package tn.esprit.spring.teamsync.Controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.teamsync.Services.HuggingFaceService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "http://localhost:4200")
public class AIController {

    @Autowired
    private HuggingFaceService hfService;

    @Getter
    @Setter
    static class DescriptionRequest {
        private String taskTitle;
        private String projectContext;
        private String taskType;
        private String additionalInstructions;
        private String priority;
        private String deadline;
        private List<String> requiredSkills;


        @Override
        public String toString() {
            return String.format(
                    "taskTitle='%s', projectContext='%s', taskType='%s', additionalInstructions='%s', deadline='%s', priority='%s', requiredSkills='%s'",
                    taskTitle, projectContext, taskType, additionalInstructions, deadline, priority, requiredSkills
            );
        }
    }

    @PostMapping("/generate-description")
    public ResponseEntity<?> generateDescription(@RequestBody DescriptionRequest req) {
        System.out.println("💬 AI request: " + req);

        try {
            // Validate required field
            if (req.getTaskTitle() == null || req.getTaskTitle().isBlank()) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "Task title is required")
                );
            }

            // Simplify task type for prompt customization
            String taskType = (req.getTaskType() != null && !req.getTaskType().isBlank()) ?
                    req.getTaskType().toLowerCase() : "general";

            // Create a more effective but concise prompt for FLAN-T5
            String promptForDetails = buildPromptPart1(
                    req.getTaskTitle(),
                    req.getProjectContext(),
                    taskType,
                    req.getAdditionalInstructions(),
                    req.getDeadline(),
                    req.getPriority(),
                    req.getRequiredSkills()
            );


            // Get initial description
            String initialDesc = hfService.generateText(promptForDetails);

            // If the description seems incomplete, make a follow-up call
            if (initialDesc.length() < 100) {
                String promptForExpansion = "Expand this task description with more details: " + initialDesc;
                String additionalDetails = hfService.generateText(promptForExpansion);
                initialDesc = initialDesc + "\n\n" + additionalDetails;
            }

            // Clean and format response
            String cleanedDesc = initialDesc.replaceAll("\"", "").trim();

            return ResponseEntity.ok(Map.of("description", cleanedDesc));

        } catch (Exception hfErr) {
            System.err.println("❗ AI Service Error: " + hfErr.getMessage());
            return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of(
                            "error", "AI service unavailable",
                            "details", hfErr.getMessage() != null ? hfErr.getMessage() : "Unknown error"
                    ));
        }
    }

    private String buildPromptPart1(String title, String context, String type, String additionalInfo,
                                    String deadline, String priority, List<String> skills) {
        StringBuilder sb = new StringBuilder();

        sb.append("Task: Write a detailed, professional task description.\n");
        sb.append("Title: ").append(title).append("\n");

        if (context != null && !context.isBlank()) {
            sb.append("Context: ").append(context).append("\n");
        }

        if (type != null && !type.isBlank()) {
            sb.append("Type: ").append(type).append("\n");
        }

        if (priority != null && !priority.isBlank()) {
            sb.append("Priority: ").append(priority).append("\n");
        }

        if (deadline != null && !deadline.isBlank()) {
            sb.append("Deadline: ").append(deadline).append("\n");
        }

        if (skills != null && !skills.isEmpty()) {
            sb.append("Required Skills: ").append(String.join(", ", skills)).append("\n");
        }

        if (additionalInfo != null && !additionalInfo.isBlank() && !"null".equals(additionalInfo)) {
            sb.append("Additional Requirements: ").append(additionalInfo).append("\n");
        }

        sb.append("\nInclude: objectives, steps, technical requirements, deliverables.\n");
        sb.append("Be specific and detailed. Use technical language.\n");

        // Contextual hints
        if (type.contains("security") || title.toLowerCase().contains("security")) {
            sb.append("\nThis is a security task. Include methodologies, tools, compliance standards, reporting formats.");
        } else if (type.contains("develop") || type.contains("coding")) {
            sb.append("\nThis is a development task. Include tech stack, coding standards, testing strategies, APIs, libraries.");
        } else if (type.contains("design")) {
            sb.append("\nThis is a design task. Include design principles, Figma (or other tool) usage, feedback cycles.");
        }

        return sb.toString();
    }

}