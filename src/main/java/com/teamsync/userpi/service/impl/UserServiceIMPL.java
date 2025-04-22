package com.teamsync.userpi.service.impl;

import com.teamsync.userpi.entity.ChatMessage;
import com.teamsync.userpi.entity.User;
import com.teamsync.userpi.exception.UserCollectionException;
import com.teamsync.userpi.repository.ChatMessageRepository;
import com.teamsync.userpi.repository.UserRepository;
import com.teamsync.userpi.service.EmailService;
import com.teamsync.userpi.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceIMPL implements UserService {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;
    @Autowired
    private ChatMessageRepository chatMessageRepository;




    @Override
    @Transactional
    public User createUser(User user) throws UserCollectionException {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new UserCollectionException("Email cannot be empty");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new UserCollectionException("Password cannot be empty");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already exists"
            );
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        sendWelcomeEmail(savedUser);
        return savedUser;
    }







    @Async
    public void sendWelcomeEmail(User user) {
        try {
            String subject = "Welcome to Our Platform!";
            String htmlContent = getWelcomeEmailTemplate(user.getName());
            emailService.sendEmail(user.getEmail(), subject, htmlContent);
        } catch (Exception e) {
            System.err.println("Failed to send welcome email to " + user.getEmail() + ": " + e.getMessage());
            // Consider adding retry logic or logging to a monitoring system
        }
    }

    private String getWelcomeEmailTemplate(String userName) {
        return "<!DOCTYPE html>" +
                "<html><head><meta charset='UTF-8'></head>" +
                "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;'>" +
                "<table align='center' width='600' style='background-color: #ffffff; padding: 20px; border-radius: 8px;'>" +
                "<tr><td align='center'>" +
                "<h2 style='color: #2E86C1;'>🎉 Welcome, " + userName + "!</h2>" +
                "<p style='font-size: 16px; color: #333;'>Your account has been successfully created.</p>" +
                "<p style='font-size: 16px; color: #333;'>You can now login and start using our platform.</p>" +
                "<a href='https://yourplatform.com/login' " +
                "style='display: inline-block; padding: 10px 20px; background-color: #2E86C1; color: white; text-decoration: none; border-radius: 5px;'>Login to Your Account</a>" +
                "<p style='margin-top: 20px; font-size: 14px; color: #777;'>Thank you for joining us,</p>" +
                "<p style='font-size: 14px; color: #777;'>The Platform Team</p>" +
                "</td></tr></table></body></html>";
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.isEmpty() ? new ArrayList<>() : users;
    }

    @Override
    public User getSingleUser(String id) throws UserCollectionException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserCollectionException(
                        UserCollectionException.NotFoundException(id)
                ));
    }

    @Override
    public void updateUser(String id, User updatedUser) throws UserCollectionException {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserCollectionException("User not found with id: " + id));

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setRole(updatedUser.getRole());
        existingUser.setTelephone(updatedUser.getTelephone());
        existingUser.setPhotoUrl(updatedUser.getPhotoUrl());

        // Re-hash the password only if it's not already hashed (starts with $2a$)
        if (!updatedUser.getPassword().startsWith("$2a$")) {
            // Plain password - hash it
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        } else {
            // Already encoded, store as-is
            existingUser.setPassword(updatedUser.getPassword());
        }


        userRepository.save(existingUser);
    }


    @Override
    public void deleteUserById(String id) throws UserCollectionException {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new UserCollectionException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }


    @Override
    public User loginUser(String email, String password) throws UserCollectionException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserCollectionException("User not found with this email"));

        if ("Inactive".equalsIgnoreCase(user.getStatus())) {
            throw new UserCollectionException("User account is inactive. Please contact admin.");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserCollectionException("Incorrect password");
        }

        return user;
    }

    @Override
    public void unblockUserById(String id) throws UserCollectionException {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new UserCollectionException("User not found with id " + id);
        }
        User user = userOptional.get();
        user.setStatus("Active");  // Unblock = Active
        userRepository.save(user);
    }

    @Override
    public void initiatePasswordReset(String email) throws UserCollectionException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new UserCollectionException("User not found with email: " + email);
        }

        User user = optionalUser.get();
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        userRepository.save(user);

        String resetUrl = "http://localhost:4200/resetpassword?token=" + token;

        String emailBody = """
    <!DOCTYPE html>
    <html lang="en">
    <head>
      <meta charset="UTF-8">
      <title>Reset Your Password</title>
    </head>
    <body style="margin:0; padding:0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f6f8;">
      <div style="max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); overflow: hidden;">
        <div style="background-color: #ffecb3; padding: 20px; text-align: center;">
          <h2 style="margin: 0; color: #f57c00;">🌞 Password Reset Request</h2>
        </div>
        <div style="padding: 30px;">
          <p style="font-size: 16px; color: #333;">Hello <strong>%s</strong>,</p>
          <p style="font-size: 16px; color: #333;">We received a request to reset your password.</p>
          <p style="text-align: center;">
            <a href="%s" 
               style="display: inline-block; padding: 12px 24px; margin-top: 20px; font-size: 16px; color: #fff; background-color: #ff9800; text-decoration: none; border-radius: 50px; font-weight: bold;">
              🔒 Reset Password
            </a>
          </p>
          <p style="font-size: 14px; color: #888; margin-top: 30px;">If you didn’t request this, you can safely ignore this email.</p>
          <p style="font-size: 14px; color: #888;">Thanks, <br> The TeamSync Team</p>
        </div>
        <div style="background-color: #f0f0f0; text-align: center; padding: 15px;">
          <small style="color: #888;">© 2025 TeamSync. All rights reserved.</small>
        </div>
      </div>
    </body>
    </html>
    """.formatted(user.getName(), resetUrl);
        try {
            emailService.sendEmail(user.getEmail(), "Reset Your Password", emailBody);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send reset email", e);
        }
    }

    @Override
    public void resetPassword(String token, String newPassword) throws UserCollectionException {
        Optional<User> optionalUser = userRepository.findByResetToken(token);
        if (optionalUser.isEmpty()) {
            throw new UserCollectionException("Invalid reset token");
        }

        User user = optionalUser.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        userRepository.save(user);
    }



    @Override
    public void blockUserById(String id) throws UserCollectionException {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new UserCollectionException("User not found with id " + id);
        }
        User user = userOptional.get();
        user.setStatus("Inactive");  // ✅ Make sure it sets to "Inactive"
        userRepository.save(user);
    }

    @Override
    public void saveChatMessage(String userId, ChatMessage chatMessage) {
        chatMessage.setUserId(userId);
        chatMessageRepository.save(chatMessage);
    }

    @Override
    public List<ChatMessage> getChatHistory(String userId) {
        return chatMessageRepository.findByUserIdOrderByTimestampAsc(userId);
    }


}


