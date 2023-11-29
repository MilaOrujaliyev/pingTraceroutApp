package com.Bil429Project.pingTraceroutApp.repository;

import com.Bil429Project.pingTraceroutApp.entity.TracerouteResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TracerouteResultRepository extends JpaRepository<TracerouteResult, Long> {
    Optional<TracerouteResult> findTopByOrderByIdDesc();
}

