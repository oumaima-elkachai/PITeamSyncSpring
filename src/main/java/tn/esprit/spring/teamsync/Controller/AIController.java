package tn.esprit.spring.teamsync.Controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.teamsync.Services.HuggingFaceService;
import tn.esprit.spring.teamsync.Services.OpenAIService;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "http://localhost:4200")
public class AIController {

    @Autowired private OpenAIService       openAI;
    @Autowired private HuggingFaceService hfService;

    @Getter @Setter
    static class DescriptionRequest {
        private String taskTitle;
        private String projectContext;
        private String taskType;

        @Override
        public String toString() {
            return String.format(
                    "taskTitle='%s', projectContext='%s', taskType='%s'",
                    taskTitle, projectContext, taskType
            );
        }
    }

    @PostMapping("/generate-description")
    public ResponseEntity<?> generateDescription(@RequestBody DescriptionRequest req) {
        System.out.println("💬 AI request: " + req);
        // 1) Try OpenAI
        try {
            String description = openAI.generateTaskDescription(
                    req.getTaskTitle(), req.getProjectContext(), req.getTaskType()
            );
            return ResponseEntity.ok(Map.of("description", description));
        } catch (Exception openAiErr) {
            System.err.println("❗ OpenAI failed: " + openAiErr.getMessage());
            // 2) Fallback to Hugging Face
            try {
                String prompt = String.format(
                        "Write a clear, actionable task description (≤100 words) for “%s”. Context: %s; Type: %s.",
                        req.getTaskTitle(), req.getProjectContext(), req.getTaskType()
                );
                String desc = hfService.generateText(prompt);
                return ResponseEntity.ok(Map.of("description", desc));
            } catch (Exception hfErr) {
                hfErr.printStackTrace();
                return ResponseEntity
                        .status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body(Map.of(
                                "error",   "AI services unavailable",
                                "details", hfErr.getMessage()
                        ));
            }
        }
    }
}
