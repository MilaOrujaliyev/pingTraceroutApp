package com.Bil429Project.pingTraceroutApp.repository;

import com.Bil429Project.pingTraceroutApp.entity.TracerouteResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface TracerouteResultRepository extends JpaRepository<TracerouteResult, Long> {
    Optional<TracerouteResult> findTopByOrderByIdDesc();

    List<TracerouteResult> findTop5ByOrderByCreatedTimeDesc();

}

