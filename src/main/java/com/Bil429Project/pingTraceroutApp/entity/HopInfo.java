package com.Bil429Project.pingTraceroutApp.entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
public class HopInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer hopNumber;
    private String country;
    private String town;
    private Double latitude;
    private Double longitude;
    private String ip;
    private String hostname;

    @ElementCollection
    private List<Integer> latencies;

    @ManyToOne
    @JoinColumn(name = "traceroute_result_id")
    private TracerouteResult tracerouteResult;

    // Getter ve setter metodları...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getHopNumber() {
        return hopNumber;
    }

    public void setHopNumber(Integer hopNumber) {
        this.hopNumber = hopNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public List<Integer> getLatencies() {
        return latencies;
    }

    public void setLatencies(List<Integer> latencies) {
        this.latencies = latencies;
    }

    public TracerouteResult getTracerouteResult() {
        return tracerouteResult;
    }

    public void setTracerouteResult(TracerouteResult tracerouteResult) {
        this.tracerouteResult = tracerouteResult;
    }

    public HopInfo(Long id, Integer hopNumber, String country, String town, Double latitude, Double longitude, String ip, String hostname, List<Integer> latencies, TracerouteResult tracerouteResult) {
        this.id = id;
        this.hopNumber = hopNumber;
        this.country = country;
        this.town = town;
        this.latitude = latitude;
        this.longitude = longitude;
        this.ip = ip;
        this.hostname = hostname;
        this.latencies = latencies;
        this.tracerouteResult = tracerouteResult;
    }

    public HopInfo() {
    }
}



