package com.Bil429Project.pingTraceroutApp.service;

import com.Bil429Project.pingTraceroutApp.entity.PingResult;
import com.Bil429Project.pingTraceroutApp.repository.PingResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
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

        String command = String.format("ping -n 4 %s", target);

        try {
            Process process = new ProcessBuilder(command.split(" ")).start();
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {

                if(line.contains("Ping request could not")) {
                    result.setErrorMessage(line);
                }

                if (line.contains("Reply from")) {
                    String[] parts = line.split(" ");
                    String ip = parts[2].replace(":", "");
                    result.setIpConnectedTo(ip);

                    bytes.add(Integer.parseInt(parts[3].split("=")[1]));
                    responseTimes.add(Integer.valueOf(parts[4].split("=")[1].replace("ms", "")));
                    ttls.add(Integer.parseInt(parts[5].split("=")[1]));
                }
                if (line.contains("Request timed out")) {
                    result.setIpConnectedTo("");
                    bytes.add(null);
                    responseTimes.add(null);
                    ttls.add(null);
                    result.setIpConnectedTo(null);
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
                if (line.contains("Packets: Sent")) {
                    String[] parts = line.split(",");

                    String sentPart = parts[0].trim();
                    int sent = Integer.parseInt(sentPart.split("=")[1].trim());

                    String receivedPart = parts[1].trim();
                    int received = Integer.parseInt(receivedPart.split("=")[1].trim());

                    String lostPart = parts[2].trim();
                    int lost = Integer.parseInt(lostPart.split("=")[1].trim().split(" ")[0]);
                    result.setTotalLostPackets(lost);

                    double loss = 100.0 * lost / sent;

                    result.setPacketLossPercentage(loss);

                    result.setTotalSentPackets(sent);
                    result.setTotalReceivedPackets(received);
                }
            }
            result.setResponseTimes(responseTimes);
            result.setTtls(ttls);
            result.setBytes(bytes);


            result.setCreatedTime(new Date());

            input.close();
            pingResultRepository.save(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
