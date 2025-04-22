package com.teamsync.userpi.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat")
public class ChatMessage {
    @Id
    private String id;

    private String userId; // Reference to the user
    private String sender; // "user" or "bot"
    private String message;
    private LocalDateTime timestamp;
}
