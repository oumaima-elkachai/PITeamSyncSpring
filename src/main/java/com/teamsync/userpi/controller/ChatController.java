/*package com.teamsync.userpi.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @PostMapping("/ask")
    public Map<String, String> ask(@RequestBody Map<String, String> payload) {
        String question = payload.get("question");
        String response = chatClient.prompt()
                .user(question)
                .call()
                .content();
        return Map.of("answer", response);
    }
}*/
