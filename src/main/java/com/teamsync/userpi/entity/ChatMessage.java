package com.teamsync.userpi.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat")
public class ChatMessage {
    @Id
    private String id;
    private String userId;
    private String conversationId;
    private String sender;
    private String message;
    private LocalDateTime timestamp;
    private String title;

}
