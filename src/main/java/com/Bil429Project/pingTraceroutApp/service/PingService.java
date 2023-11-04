package com.Bil429Project.pingTraceroutApp.service;

import com.Bil429Project.pingTraceroutApp.entity.PingResult;
import com.Bil429Project.pingTraceroutApp.repository.PingResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;

@Service
public class PingService {

    @Autowired
    private PingResultRepository pingResultRepository;

    public PingResult ping(String target) {
        PingResult result = new PingResult();
        result.setTarget(target);
        List<Integer> responseTimes = new ArrayList<>();
        List<Integer> ttls = new ArrayList<>();
        List<Integer> bytes = new ArrayList<>();

        String command;

        if (System.getProperty("os.name").startsWith("Windows")) {
            command = String.format("ping -n 4 %s", target);
        } else {
            command = String.format("ping -c 4 %s", target);
        }

        try {
            Process process = new ProcessBuilder(command.split(" ")).start();
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                if (line.contains("Reply from")) {
                    // Örneğin: Reply from 172.217.17.142: bytes=32 time=22ms TTL=57
                    String[] parts = line.split(" ");
                    // IP adresini çıkarıyoruz
                    String ip = parts[2].replace(":", "");
                    result.setIpConnectedTo(ip); // PingResult sınıfındaki ipConnectedTo özelliğine atıyoruz

                    bytes.add(Integer.parseInt(parts[3].split("=")[1]));
                    responseTimes.add(Integer.valueOf(parts[4].split("=")[1].replace("ms", "")));
                    ttls.add(Integer.parseInt(parts[5].split("=")[1]));
                }
                if (line.contains("Average")) {
                    String[] parts = line.split("=");
                    result.setAverageResponseTime(Double.valueOf(parts[2].trim().split("ms")[0].trim()));
                }
                if (line.contains("Minimum")) {
                    String[] parts = line.split("=");
                    result.setMinResponseTime(Double.valueOf(parts[1].trim().split("ms")[0].trim()));
                    result.setMaxResponseTime(Double.valueOf(parts[2].trim().split("ms")[0].trim()));
                }
                if (line.contains("packets transmitted")) {
                    String[] parts = line.split(" ");
                    int total = Integer.parseInt(parts[0]);
                    int received = Integer.parseInt(parts[3]);
                    double loss = 100.0 - (100.0 * received / total);
                    result.setPacketLossPercentage(loss);
                }
//                if (line.contains("Lost")) {
//                    String[] parts = line.split("Lost =");
//                    String lostStr = parts[1].trim().split(" ")[0].trim();
//                    int lost = Integer.parseInt(lostStr.replace(",", ""));
//
//                    int total = 4;//result.getTotalSentPackets();
//                    double loss = 100.0 * lost / total;
//                    result.setPacketLossPercentage(loss);
//                    result.setTotalLostPackets(lost);
//                }
                if (line.contains("Packets: Sent")) {
                    // Satırı virgülle bölüp her bir bölümü ayrı ayrı işleyeceğiz
                    String[] parts = line.split(",");

                    // "Sent = 4" kısmını parse edelim
                    String sentPart = parts[0].trim(); // "Packets: Sent = 4"
                    int sent = Integer.parseInt(sentPart.split("=")[1].trim()); // "=" işaretinden bölüp ikinci kısmı alıyoruz ve trim ile başındaki ve sonundaki boşluklardan kurtuluyoruz

                    // "Received = 4" kısmını parse edelim
                    String receivedPart = parts[1].trim(); // " Received = 4"
                    int received = Integer.parseInt(receivedPart.split("=")[1].trim());

                    // "Lost = 0 (0% loss)" kısmını parse edelim (bu örnekte gerek yok çünkü zaten "Lost" kısmını işliyoruz ama örnek olması açısından)
                    String lostPart = parts[2].trim(); // " Lost = 0 (0% loss)"
                    int lost = Integer.parseInt(lostPart.split("=")[1].trim().split(" ")[0]); // Kayıp paket sayısını alıyoruz
                    result.setTotalLostPackets(lost);

                    double loss = 100.0 * lost / sent;

                    result.setPacketLossPercentage(loss);

                    // Alınan değerleri PingResult nesnesine atayalım
                    result.setTotalSentPackets(sent);
                    result.setTotalReceivedPackets(received);
                    // Kayıp paket sayısını ve yüzdesini zaten hesaplamıştık, gerekirse burada da tekrar hesaplayabiliriz.
                }
            }
            result.setResponseTimes(responseTimes);
            result.setTtls(ttls);
            result.setBytes(bytes);

            input.close();
            pingResultRepository.save(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
