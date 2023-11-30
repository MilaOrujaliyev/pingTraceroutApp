package com.Bil429Project.pingTraceroutApp.service;

import com.Bil429Project.pingTraceroutApp.entity.HopInfo;
import com.Bil429Project.pingTraceroutApp.entity.TracerouteResult;
import com.Bil429Project.pingTraceroutApp.model.IpAPIModel;
import com.Bil429Project.pingTraceroutApp.repository.TracerouteResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TracerouteService {
    @Autowired
    private TracerouteResultRepository repository;

    @Autowired
    private  ApiService apiService;

    public TracerouteResult performTraceroute(String target) {
        TracerouteResult tracerouteResult = new TracerouteResult();
        tracerouteResult.setTarget(target);
        tracerouteResult.setCreatedTime(new Date());

        List<HopInfo> hops = new ArrayList<>();

        //List<TracerouteResult.HopInfo> hops = new ArrayList<>();

        // Traceroute komutunun çalıştırılması
        String command = "tracert " + target;


        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                // Gelen satırı işleyerek HopInfo nesnesini oluşturun ve doldurun
                HopInfo hop = parseTracerouteLine(inputLine);

                if (hop != null) {
                    hop.setTracerouteResult(tracerouteResult);


                    IpAPIModel ipAPIModel= apiService.getIpDetails("http://ip-api.com/json/"+hop.getIp());
                    if(ipAPIModel.getStatus().equals("success")){
                        hop.setCountry(ipAPIModel.getCountry());
                        hop.setCountryCode(ipAPIModel.getCountryCode());
                        hop.setRegion(ipAPIModel.getRegion());
                        hop.setRegionName(ipAPIModel.getCity());
                        hop.setCity(ipAPIModel.getRegion());
                        hop.setZip(ipAPIModel.getZip());
                        hop.setLatitude(ipAPIModel.getLat());
                        hop.setLongitude(ipAPIModel.getLon());
                        hop.setTimezone(ipAPIModel.getTimezone());
                        hop.setIsp(ipAPIModel.getIsp());
                        hop.setOrg(ipAPIModel.getOrg());
                        hop.setAsCode(ipAPIModel.getAs());

                        if(hop.getCountryCode() != null) {
                            String flagUrl = "https://flagcdn.com/24x18/" + hop.getCountryCode().toLowerCase() + ".png";
                            hop.setFlagUrl(flagUrl);
                        }
                    }

                    hops.add(hop);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        calculateDistancesAndLatencies(hops);
        tracerouteResult.setHops(hops);
        repository.save(tracerouteResult);

        return tracerouteResult;
    }

    private HopInfo parseTracerouteLine(String line) {
        // Önce satırın boşluklarını temizleyin ve parçalara ayırın
        String[] parts = line.trim().split("\\s+");

        // Hop numarası ve IP adresini almak için kontrol edin
        if (parts.length >= 4 && parts[1].matches("\\d+")) {
            HopInfo hopInfo = new HopInfo();
            hopInfo.setHopNumber(Integer.parseInt(parts[0]));

            // IP adresi ve hostname'i ayıklama
            if (parts[2].equals("*")) {
                hopInfo.setIp("Request timed out.");
            } else {
               String ip= parts[parts.length - 1].replace("[", "").replace("]", "");
                hopInfo.setIp(ip); // IP adresi satırın sonunda yer alır
            }

            // Gecikme sürelerini ayıklama
            List<Integer> latencies = new ArrayList<>();
            for (int i = 1; i < parts.length - 1; i++) {
                if (parts[i].endsWith("ms")) {
                    int latencyValue = Integer.parseInt(parts[i-1].trim());
                    latencies.add(latencyValue);
                } else {
                    latencies.add(null); // Zaman aşımı durumlarında null değer içeren gecikme süresi
                }
            }
            hopInfo.setLatencies(latencies);

            return hopInfo;
        }
        return null; // Satır uygun formatta değilse null dön
    }


    public List<TracerouteResult> getAllTracerouteResults() {
        return repository.findAll();
    }

    public TracerouteResult getTracerouteResultById(Long id) throws Exception {
        return repository.findById(id).orElseThrow(() -> new Exception("TracerouteResult not found with id: " + id));
    }

    public TracerouteResult getLastTracerouteResult() throws Exception {
        // Veritabanından en son eklenen TracerouteResult nesnesini bulma
        // Bu örnekte JPARepository'nin sağladığı findOne özelliği kullanılmıştır.
        // Uygulamanızın ihtiyaçlarına göre bu kısım değişebilir.
        return repository.findTopByOrderByIdDesc()
                .orElseThrow(() -> new Exception("No traceroute results found."));
    }

    public List<TracerouteResult> getLastFiveTracerouteResults() {
        return repository.findTop5ByOrderByCreatedTimeDesc();
    }

    // Belirli bir hedef için en son traceroute sonucunu döndüren metot
    public TracerouteResult getTracerouteByTarget(String target) {
        return repository.findTopByTargetOrderByCreatedTimeDesc(target)
                .orElseThrow(() -> new RuntimeException("Traceroute result not found for target: " + target));
    }

    private void calculateDistancesAndLatencies(List<HopInfo> hops) {
        // İlk hop için koşulları kontrol et
        boolean firstCoordinateFound = false;
        for (int i = 0; i < hops.size(); i++) {
            HopInfo currentHop = hops.get(i);

            // İlk koordinat bulunduysa veya koordinat bilgisi yoksa distance = 0.00 olarak ayarla
            if ((currentHop.getLatitude() != null && currentHop.getLongitude() != null) || i == 0) {
                currentHop.setDistance(0.00);
                firstCoordinateFound = true; // İlk koordinatı işaret et
            }

            if (firstCoordinateFound && currentHop.getLatitude() == null && currentHop.getLongitude() == null) {
                // İlk koordinat bulunduktan sonra koordinat bilgisi olmayan hoplar için
                currentHop.setDistance(0.00);
            }

            if (i > 0 && firstCoordinateFound) {
                HopInfo previousHop = hops.get(i - 1);
                if (previousHop.getLatitude() != null && previousHop.getLongitude() != null &&
                        currentHop.getLatitude() != null && currentHop.getLongitude() != null) {
                    double distance = calculateDistance(
                            previousHop.getLatitude(), previousHop.getLongitude(),
                            currentHop.getLatitude(), currentHop.getLongitude()
                    );
                    currentHop.setDistance(Math.round(distance * 100.0) / 100.0); // Yuvarlama işlemi ile 2 ondalık basamak
                }
            }

            if (currentHop.getLatencies() != null && !currentHop.getLatencies().isEmpty()) {
                currentHop.setAverageLatency(calculateAverageLatency(currentHop.getLatencies()));
            }
        }
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Dünya'nın yarıçapı kilometre cinsinden
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // hesaplanan mesafe kilometre cinsinden

        return Math.round(distance * 100.0) / 100.0; // Yuvarlama işlemi ile 2 ondalık basamak
    }

    private double calculateAverageLatency(List<Integer> latencies) {
        if (latencies == null || latencies.isEmpty()) {
            // Gecikme süreleri listesi boş veya null ise, ortalama hesaplanamaz
            return 0.00;
        }

        double sum = 0;
        int count = 0;
        for (Integer latency : latencies) {
            if (latency != null) {
                sum += latency;
                count++;
            }
        }

        return count > 0 ? Math.round((sum / count) * 100.0) / 100.0 : 0.00; // Yuvarlama işlemi ile 2 ondalık basamak
    }


}
