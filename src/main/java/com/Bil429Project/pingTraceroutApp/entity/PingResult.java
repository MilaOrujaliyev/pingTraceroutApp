package com.Bil429Project.pingTraceroutApp.entity;


import jakarta.persistence.*;

import java.util.List;

@Entity
public class PingResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String target;

    private String ipConnectedTo;

    // Tek tek ping sonuçları için bilgiler
    @ElementCollection
    private List<Integer> responseTimes; // Tüm yanıt süreleri (ms)
    @ElementCollection
    private List<Integer> bytes; // Her ping için giden bayt miktarı
    @ElementCollection
    private List<Integer> ttls; // Her ping için TTL değerleri

    private Double averageResponseTime;
    private Double maxResponseTime;
    private Double minResponseTime;
    private Double packetLossPercentage;
    private Integer totalSentPackets; // Gönderilen toplam paket sayısı
    private Integer totalReceivedPackets; // Alınan toplam paket sayısı
    private Integer totalLostPackets; // Kaybedilen toplam paket sayısı

    public String getIpConnectedTo() {
        return ipConnectedTo;
    }

    public void setIpConnectedTo(String ipConnectedTo) {
        this.ipConnectedTo = ipConnectedTo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public List<Integer> getResponseTimes() {
        return responseTimes;
    }

    public void setResponseTimes(List<Integer> responseTimes) {
        this.responseTimes = responseTimes;
    }

    public List<Integer> getBytes() {
        return bytes;
    }

    public void setBytes(List<Integer> bytes) {
        this.bytes = bytes;
    }

    public List<Integer> getTtls() {
        return ttls;
    }

    public void setTtls(List<Integer> ttls) {
        this.ttls = ttls;
    }

    public Double getAverageResponseTime() {
        return averageResponseTime;
    }

    public void setAverageResponseTime(Double averageResponseTime) {
        this.averageResponseTime = averageResponseTime;
    }

    public Double getMaxResponseTime() {
        return maxResponseTime;
    }

    public void setMaxResponseTime(Double maxResponseTime) {
        this.maxResponseTime = maxResponseTime;
    }

    public Double getMinResponseTime() {
        return minResponseTime;
    }

    public void setMinResponseTime(Double minResponseTime) {
        this.minResponseTime = minResponseTime;
    }

    public Double getPacketLossPercentage() {
        return packetLossPercentage;
    }

    public void setPacketLossPercentage(Double packetLossPercentage) {
        this.packetLossPercentage = packetLossPercentage;
    }

    public Integer getTotalSentPackets() {
        return totalSentPackets;
    }

    public void setTotalSentPackets(Integer totalSentPackets) {
        this.totalSentPackets = totalSentPackets;
    }

    public Integer getTotalReceivedPackets() {
        return totalReceivedPackets;
    }

    public void setTotalReceivedPackets(Integer totalReceivedPackets) {
        this.totalReceivedPackets = totalReceivedPackets;
    }

    public Integer getTotalLostPackets() {
        return totalLostPackets;
    }

    public void setTotalLostPackets(Integer totalLostPackets) {
        this.totalLostPackets = totalLostPackets;
    }
}
