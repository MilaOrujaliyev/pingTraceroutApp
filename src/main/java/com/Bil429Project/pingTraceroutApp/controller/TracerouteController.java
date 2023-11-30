package com.Bil429Project.pingTraceroutApp.controller;

import com.Bil429Project.pingTraceroutApp.entity.TracerouteResult;
import com.Bil429Project.pingTraceroutApp.service.TracerouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//@CrossOrigin(origins = "http://localhost:8081/traceroute/")

@RestController
@RequestMapping("/api")
public class TracerouteController {

    @Autowired
    private TracerouteService tracerouteService;

    // ID'ye göre bir TracerouteResult ve ilişkili HopInfo nesnelerini döndürür
    // Tüm traceroute sonuçlarını getir
    @GetMapping("/traceroutes")
    public List<TracerouteResult> getAllTracerouteResults() {
        return tracerouteService.getAllTracerouteResults();
    }

    // Belirli bir traceroute sonucunu getir
    @GetMapping("/traceroutes/{id}")
    public TracerouteResult getTracerouteResult(@PathVariable Long id) throws Exception {
        return tracerouteService.getTracerouteResultById(id);
    }

    @GetMapping("/traceroutes/last")
    public ResponseEntity<TracerouteResult> getLastTracerouteResult() {
        try {
            TracerouteResult lastTracerouteResult = tracerouteService.getLastTracerouteResult();
            return ResponseEntity.ok(lastTracerouteResult);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/traceroutes/last5")
    public ResponseEntity<List<TracerouteResult>> getLastFiveTracerouteResults() {
        List<TracerouteResult> lastFiveResults = tracerouteService.getLastFiveTracerouteResults();
        return ResponseEntity.ok(lastFiveResults);
    }
    @GetMapping("/traceroutes/specific")
    public ResponseEntity<TracerouteResult> getTracerouteByTarget(@RequestParam String target) {
        // Servis katmanını kullanarak belirli bir hedef için traceroute verilerini alın
        TracerouteResult tracerouteResult = tracerouteService.getTracerouteByTarget(target);
        if (tracerouteResult != null) {
            return ResponseEntity.ok(tracerouteResult);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
