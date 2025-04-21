package com.example.attendance.Backend.services.interfaces;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BadgeService {
    List<String> getBadgesForEmployee(String employeeId);

}
