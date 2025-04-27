package tn.esprit.spring.teamsync.Services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class HuggingFaceService {
    private final RestTemplate restTemplate;
    private final String apiToken;
    private final String apiUrl;
    private final Double temperature;
    private final Integer maxTokens;

    public HuggingFaceService(
            @Value("${hf.api.token}") String apiToken,
            @Value("${hf.api.url}") String apiUrl,
            @Value("${hf.temperature:0.8}") Double temperature,
            @Value("${hf.max_tokens:250}") Integer maxTokens
    ) {
        this.restTemplate = new RestTemplate();
        this.apiToken = apiToken;
        this.apiUrl = apiUrl;
        this.temperature = temperature;
        this.maxTokens = maxTokens;
        System.out.println("🦅 Hugging Face configured for model: " + apiUrl);
    }

    public String generateText(String prompt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiToken);

            // Create a properly structured request body with optimized parameters
            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("inputs", prompt);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("temperature", temperature);
            parameters.put("max_new_tokens", maxTokens);
            parameters.put("do_sample", true);
            parameters.put("top_p", 0.92);
            parameters.put("top_k", 50);
            parameters.put("repetition_penalty", 1.2);  // Discourage repetition

            Map<String, Object> options = new HashMap<>();
            options.put("wait_for_model", true);
            options.put("use_cache", false);  // Force fresh generation

            requestMap.put("parameters", parameters);
            requestMap.put("options", options);

            ObjectMapper mapper = new ObjectMapper();
            String requestBody = mapper.writeValueAsString(requestMap);

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    apiUrl,
                    entity,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return cleanResponse(response.getBody());
            }

            throw new RuntimeException(
                    "HF API Error: " + response.getStatusCode() + " - " + response.getBody()
            );

        } catch (Exception e) {
            throw new RuntimeException("Hugging Face service unavailable: " + e.getMessage());
        }
    }

    private String cleanResponse(String jsonResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonResponse);
            return root.get(0).get("generated_text").asText();
        } catch (Exception e) {
            return "Could not parse AI response";
        }
    }
}