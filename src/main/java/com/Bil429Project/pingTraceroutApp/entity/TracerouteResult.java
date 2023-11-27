package com.Bil429Project.pingTraceroutApp.entity;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class TracerouteResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String target;
    private Date createdTime;

    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


    @OneToMany(mappedBy = "tracerouteResult", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HopInfo> hops;

    // Constructor
    public TracerouteResult() {
        this.hops = new ArrayList<>();
    }

    // Getter ve setter metodları
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

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public List<HopInfo> getHops() {
        return hops;
    }

    public void setHops(List<HopInfo> hops) {
        this.hops.clear();
        if (hops != null) {
            this.hops.addAll(hops);
        }
    }

    // HopInfo nesnesi eklemek için yardımcı metod
    public void addHop(HopInfo hop) {
        hops.add(hop);
        hop.setTracerouteResult(this);
    }

    // HopInfo nesnesini kaldırmak için yardımcı metod
    public void removeHop(HopInfo hop) {
        hops.remove(hop);
        hop.setTracerouteResult(null);
    }
}
