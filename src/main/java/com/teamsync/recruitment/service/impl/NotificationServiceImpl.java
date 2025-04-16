package com.teamsync.recruitment.service.impl;

import com.teamsync.recruitment.service.interfaces.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Override
    public void sendNotification(String title, String message) {
        System.out.println("📢 [NOTIFICATION] " + title + ": " + message);
    }
}
