package com.example.attendance.Backend.controllers;

import com.example.attendance.Backend.entity.Request;
import com.example.attendance.Backend.entity.RequestStatus;
import com.example.attendance.Backend.repository.RequestRepository;
import com.example.attendance.Backend.services.interfaces.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestRepository requestRepository;
    private final RequestService requestService;

    @PostMapping("/create")
    public Request createRequest(@RequestBody Request request) {
        request.setStatus(RequestStatus.PENDING);
        if (request.getEmployeeId() != null) {
            request.setEmployeeId(request.getEmployeeId().trim());
        }
        return requestRepository.save(request);
    }

    @GetMapping("/all")
    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    @GetMapping("/employee")
    public ResponseEntity<List<Request>> getRequestsByEmployee(@RequestParam String employeeId) {
        System.out.println("Received request for employee ID: " + employeeId);
        try {
            List<Request> requests = requestService.getRequestsByEmployeeId(employeeId.trim());
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            System.err.println("Error fetching requests: " + e.getMessage());
            e.printStackTrace(); // Print the stack trace for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @PutMapping("/{id}/status")
    public Request updateStatus(@PathVariable String id, @RequestParam String status) {
        Optional<Request> optionalRequest = requestRepository.findById(id);
        if (optionalRequest.isPresent()) {
            Request request = optionalRequest.get();
            request.setStatus(RequestStatus.valueOf(status));
            return requestRepository.save(request);
        }
        throw new RuntimeException("Request not found");
    }

    @GetMapping("/{id}")
    public Request getRequestById(@PathVariable String id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
    }

    @PutMapping("/{id}")
    public Request updateRequest(@PathVariable String id, @RequestBody Request updatedRequest) {
        return requestRepository.findById(id).map(existing -> {
            existing.setType(updatedRequest.getType());
            existing.setStartDate(updatedRequest.getStartDate());
            existing.setEndDate(updatedRequest.getEndDate());
            existing.setJustification(updatedRequest.getJustification());
            if (updatedRequest.getEmployeeId() != null) {
                existing.setEmployeeId(updatedRequest.getEmployeeId().trim());
            }
            return requestRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Request not found"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRequest(@PathVariable String id) {
        requestRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }


}
