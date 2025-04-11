package com.example.attendance.Backend.services.interfaces;


import com.example.attendance.Backend.entity.Request;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RequestService {
    List<Request> getRequestsByEmployeeId(String employeeId);

}
