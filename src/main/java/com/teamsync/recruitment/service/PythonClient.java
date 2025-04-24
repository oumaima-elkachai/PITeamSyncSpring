package com.teamsync.recruitment.service;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class PythonClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public double getMatchingScore(String jobDescription, String cvUrl) {
        String pythonServiceUrl = "http://localhost:5000/score";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("job_description", jobDescription);
        requestBody.put("cv_url", cvUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Double> response = restTemplate.exchange(
                pythonServiceUrl,
                HttpMethod.POST,
                requestEntity,
                Double.class
        );

        return response.getBody();
    }
}
