package tn.esprit.spring.teamsync.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class HuggingFaceService {

    private final RestTemplate rest = new RestTemplate();
    private final String hfToken;
    private final String model;

    public HuggingFaceService(
            @Value("${hf.api.token}") String hfToken,
            @Value("${hf.model}")     String model
    ) {
        if (hfToken == null || hfToken.isBlank()) {
            throw new IllegalStateException("Missing Hugging Face API token (hf.api.token)");
        }
        this.hfToken = hfToken;
        this.model   = model;
    }

    /**
     * Sends the prompt to HF Inference API and returns the generated text.
     */
    public String generateText(String prompt) {
        String url = "https://api-inference.huggingface.co/models/" + model;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(hfToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String,Object> payload = Map.of(
                "inputs", prompt,
                // drop wait_for_model entirely
                "parameters", Map.of("max_new_tokens", 60)
        );


        HttpEntity<Map<String,Object>> request = new HttpEntity<>(payload, headers);
        ResponseEntity<List> response = rest.exchange(url, HttpMethod.POST, request, List.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && !response.getBody().isEmpty()) {
            Object first = response.getBody().get(0);
            if (first instanceof Map) {
                Object txt = ((Map<?,?>)first).get("generated_text");
                if (txt != null) {
                    return txt.toString().trim();
                }
            }
        }
        throw new RuntimeException("Hugging Face inference failed or returned no text");
    }
}
