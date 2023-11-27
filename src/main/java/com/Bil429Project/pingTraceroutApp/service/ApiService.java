package com.Bil429Project.pingTraceroutApp.service;

import com.Bil429Project.pingTraceroutApp.model.IpAPIModel;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService {

    private final RestTemplate restTemplate;

    public ApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public IpAPIModel getIpDetails(String url) {
        return restTemplate.getForObject(url, IpAPIModel.class);
    }
}