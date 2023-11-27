package com.Bil429Project.pingTraceroutApp.service;

import com.Bil429Project.pingTraceroutApp.entity.HopInfo;
import com.Bil429Project.pingTraceroutApp.entity.TracerouteResult;
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
                    // Bayrak URL'si ekleme (bayrak servisinize göre değişiklik gösterebilir)
                    //hop.setFlagUrl("https://flagcdn.com/h20/" + hop.getCountry().toLowerCase() + ".png"); //TODO:flag info ekle
                    hops.add(hop);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
                hopInfo.setIp(parts[parts.length - 1]); // IP adresi satırın sonunda yer alır
            }

            // Gecikme sürelerini ayıklama
            List<Integer> latencies = new ArrayList<>();
            for (int i = 1; i < parts.length - 1; i++) {
                if (parts[i].endsWith("ms")) {
                    int latencyValue = Integer.parseInt(parts[i].replace("ms", "").trim());
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

}
