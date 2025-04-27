package com.example.attendance.Backend.dto;

import lombok.Data;

@Data
public class FaceCheckInRequestDTO {
    private String employeeId;
    private String imageData;
}

