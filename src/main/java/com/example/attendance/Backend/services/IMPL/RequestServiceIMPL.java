package com.example.attendance.Backend.services.IMPL;

import com.example.attendance.Backend.entity.Request;
import com.example.attendance.Backend.repository.RequestRepository;
import com.example.attendance.Backend.services.interfaces.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestServiceIMPL implements RequestService {

    private final RequestRepository requestRepository;

    @Autowired
    public RequestServiceIMPL(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public List<Request> getRequestsByEmployeeId(String employeeId) {
        return requestRepository.findByEmployeeId(employeeId);
    }
}