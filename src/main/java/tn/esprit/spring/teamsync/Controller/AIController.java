package tn.esprit.spring.teamsync.Controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.teamsync.Services.OpenAIService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "http://localhost:4200")
public class AIController {

    @Autowired
    private OpenAIService openAIService;

    @PostMapping("/generate-description")
    public ResponseEntity<?> generateDescription(@RequestBody DescriptionRequest request) {
        System.out.println("💬 AI request: " + request);
        try {
            String description = openAIService.generateTaskDescription(
                    request.getTaskTitle(),
                    request.getProjectContext(),
                    request.getTaskType()
            );
            return ResponseEntity.ok().body(Collections.singletonMap("description", description));
        } catch (Exception e) {

            e.printStackTrace();

            // 3) Return the exception message back to front-end
            Map<String,String> body = new HashMap<>();
            body.put("error", "AI service unavailable");
            body.put("details", e.getMessage());


            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "AI service unavailable"));
        }
    }

    @Getter
    @Setter // Lombok
    static class DescriptionRequest {
        private String taskTitle;
        private String projectContext;
        private String taskType;
    }
}