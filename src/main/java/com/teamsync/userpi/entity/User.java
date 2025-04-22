package com.teamsync.userpi.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.context.support.BeanDefinitionDsl;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.List;


@Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Document(collection = "users")  // Defines the MongoDB collection
    public class User {

        @Id
        private String id;  // MongoDB uses String ID (ObjectId)

        @NotNull(message = "Name can't be null")
        private String name;
        @NotNull(message = "Email can't be null")
        @Indexed(unique = true)
        private String email;
        @NotNull(message = "Password can't be null")
        private String password;
        @Enumerated(EnumType.STRING)
        @JsonProperty("role")
        private RolesUser role;
    private String status = "Active";
    private String telephone;  // e.g., "+1-234-567-890"
    private String photoUrl;
    private String resetToken;
    private List<ChatMessage> chatHistory = new ArrayList<>();


}

