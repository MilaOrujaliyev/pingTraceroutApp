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
    private String countryCode;
    private String region;
    private String regionName;
    private String city;
    private String zip;
    private Double latitude;
    private Double longitude;
    private String ip;

    private String timezone;
    private String isp;
    private String org;
    private String asCode;

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

    public HopInfo() {
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getAsCode() {
        return asCode;
    }

    public void setAsCode(String asCode) {
        this.asCode = asCode;
    }
}



