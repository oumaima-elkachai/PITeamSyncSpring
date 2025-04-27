package tn.esprit.spring.teamsync.Services;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Service
public class OpenAIService {
    private final OpenAiService openAiService;
    private final String apiKey;
    public OpenAIService(@Value("${openai.api.key:}") String apiKey) {

        this.apiKey = apiKey;
        if (apiKey.isBlank()) {
            throw new IllegalStateException("❌ openai.api.key is missing!");
        }
        System.out.println("🔑 OpenAI key loaded (" + apiKey.length() + " chars)");


        this.openAiService = new OpenAiService(apiKey, Duration.ofSeconds(30));
    }

    public String generateTaskDescription(String taskTitle, String context, String type) {
        try {
            ChatCompletionRequest request = ChatCompletionRequest.builder()
                    .model("gpt-3.5-turbo")
                    .messages(List.of(new ChatMessage("user", buildPrompt(taskTitle, context, type))))
                    .maxTokens(150)
                    .build();

            return openAiService.createChatCompletion(request)
                    .getChoices().get(0).getMessage().getContent();
        } catch (Exception e) {
            throw new RuntimeException("OpenAI API call failed: " + e.getMessage(), e);
        }
    }

    private String buildPrompt(String title, String context, String type) {
        return String.format(
                "Write a professional task description for: %s. Context: %s. Type: %s. " +
                        "Make it clear, actionable, and under 100 words.",
                title, context, type
        );
    }
}