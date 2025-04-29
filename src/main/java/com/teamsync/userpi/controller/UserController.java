package com.teamsync.userpi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamsync.userpi.DTO.LoginRequest;
import com.teamsync.userpi.entity.ChatMessage;
import com.teamsync.userpi.entity.RolesUser;
import com.teamsync.userpi.entity.User;
import com.teamsync.userpi.exception.UserCollectionException;
import com.teamsync.userpi.repository.ChatMessageRepository;
import com.teamsync.userpi.repository.UserRepository;
import com.teamsync.userpi.service.CloudinaryService;
import com.teamsync.userpi.service.EmailService;
import com.teamsync.userpi.service.FileStorageService;
import com.teamsync.userpi.service.interfaces.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.Builder;
import com.teamsync.userpi.service.JwtService; // Import JwtService


import java.net.URI;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RestController

@RequestMapping("/api/users")
public class UserController {


    private final  JwtService jwtService;

    private final transient String resetToken = null;



    private final EmailService emailService;
    private final CloudinaryService cloudinaryService;

    private final ChatMessageRepository chatMessageRepository;



    private final UserRepository userRepository;
    private final UserService userService;

    private final FileStorageService fileStorageService;

    private final ChatClient chatClient;

    @PostMapping("/chat")
    public Map<String, String> ask(@RequestBody Map<String, String> payload, HttpSession session) {
        String question = payload.get("question");
        String conversationId = payload.get("conversationId");

        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not logged in");
        }

        String response = chatClient.prompt().user(question).call().content();

        ChatMessage userMessage = ChatMessage.builder()
                .userId(user.getId())
                .conversationId(conversationId)
                .sender("user")
                .message(question)
                .timestamp(LocalDateTime.now())
                .build();

        // ✅ Set the title if this is the first message in this conversation
        boolean isFirstMessage = chatMessageRepository.findByConversationIdOrderByTimestampAsc(conversationId).isEmpty();
        if (isFirstMessage) {
            String trimmed = question.trim();
            userMessage.setTitle(trimmed.length() > 40 ? trimmed.substring(0, 40) + "..." : trimmed);
        }

        ChatMessage botMessage = ChatMessage.builder()
                .userId(user.getId())
                .conversationId(conversationId)
                .sender("bot")
                .message(response)
                .timestamp(LocalDateTime.now())
                .build();

        userService.saveChatMessage(userMessage);
        userService.saveChatMessage(botMessage);

        return Map.of("answer", response);
    }

    @GetMapping("/session-check")
    public ResponseEntity<?> sessionCheck(HttpSession session) {
        User user = (User) session.getAttribute("user");
        Map<String, Object> response = new HashMap<>();
        response.put("loggedIn", user != null);
        if (user != null) {
            response.put("user", user);
        }
        return ResponseEntity.ok(response);
    }


    @GetMapping("/chat/history")
    public List<ChatMessage> getChatHistory(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not logged in");
        }
        return userService.getChatHistory(user.getId());
    }

    @GetMapping("/chat/conversations")
    public List<String> getConversationIds(HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<ChatMessage> all = chatMessageRepository.findAllConversationIdsByUserId(user.getId());

        return all.stream()
                .map(ChatMessage::getConversationId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }


    @GetMapping("/chat/history/{conversationId}")
    public List<ChatMessage> getChatByConversation(@PathVariable String conversationId) {
        return chatMessageRepository.findByConversationIdOrderByTimestampAsc(conversationId);
    }




    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }


    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> createUser(
            @RequestPart("user") String userJson,
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            User user = objectMapper.readValue(userJson, User.class);

            if (photo != null && !photo.isEmpty()) {
                String photoUrl = cloudinaryService.uploadImage(photo);
                user.setPhotoUrl(photoUrl);
            }

            User createdUser = userService.createUser(user);
            Map<String, Object> response = Map.of(
                    "status", "success",
                    "message", "User created successfully",
                    "user", createdUser
            );
            return ResponseEntity.created(URI.create("/api/users/" + createdUser.getId())).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSingleUser(@PathVariable("id") String id) {
        try {
            return ResponseEntity.ok(userService.getSingleUser(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateById(
            @PathVariable("id") String id,
            @RequestPart("user") String userJson,
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            User updatedData = objectMapper.readValue(userJson, User.class);
            User existingUser = userService.getSingleUser(id);

            existingUser.setName(updatedData.getName());
            existingUser.setEmail(updatedData.getEmail());
            existingUser.setPassword(updatedData.getPassword());
            existingUser.setRole(updatedData.getRole());
            existingUser.setTelephone(updatedData.getTelephone());

            if (photo != null && !photo.isEmpty()) {
                String photoUrl = cloudinaryService.uploadImage(photo);
                existingUser.setPhotoUrl(photoUrl);
            }

            userService.updateUser(id, existingUser);
            User updatedUser = userService.getSingleUser(id);
            return ResponseEntity.ok(Map.of("message", "User updated", "user", updatedUser));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }




    @PutMapping("/block/{id}")
    public ResponseEntity<?> blockUser(@PathVariable("id") String id) {
        try {
            userService.blockUserById(id);  // ✅ This must match your service
            return ResponseEntity.ok("User blocked with id " + id);
        } catch (UserCollectionException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") String id) {
        try {
            userService.deleteUserById(id); // Make sure this method exists in your service
            return ResponseEntity.ok("Successfully deleted user with id " + id);
        } catch (UserCollectionException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest, HttpSession session) {
        try {
            User user = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());

            session.setAttribute("user", user);
            String safePhotoUrl = (user.getPhotoUrl() != null) ? user.getPhotoUrl() : "";

            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getId());
            userMap.put("email", user.getEmail());
            userMap.put("name", user.getName());
            userMap.put("role", user.getRole());
            userMap.put("photoUrl", safePhotoUrl);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("user", userMap);

            return ResponseEntity.ok(response);
        } catch (UserCollectionException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }

    @PutMapping("/unblock/{id}")
    public ResponseEntity<?> unblockUser(@PathVariable("id") String id) {
        try {
            userService.unblockUserById(id);  // Create this method in UserService
            return ResponseEntity.ok("User unblocked with id " + id);
        } catch (UserCollectionException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
        }

        try {
            userService.initiatePasswordReset(email);
            return ResponseEntity.ok(Map.of("message", "Reset link sent to email"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }


    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        try {
            userService.resetPassword(token, newPassword);
            return ResponseEntity.ok(Map.of("message", "Password reset successfully."));
        } catch (UserCollectionException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }





    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, users.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpSession session) throws UserCollectionException {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", "error", "message", "User not logged in"));
        }
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "user", Map.of(
                        "id", user.getId(),
                        "email", user.getEmail(),
                        "name", user.getName(),
                        "role", user.getRole(),
                        "photoUrl", user.getPhotoUrl(),
                        "telephone", user.getTelephone()
                )
        ));
    }

}
