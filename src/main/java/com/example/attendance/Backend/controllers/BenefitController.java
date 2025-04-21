package com.example.attendance.Backend.controllers;


import com.example.attendance.Backend.entity.Benefit;
import com.example.attendance.Backend.repository.BenefitRepository;
import com.example.attendance.Backend.services.interfaces.BenefitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/benefits")
@RequiredArgsConstructor
public class BenefitController {
    private final BenefitRepository benefitRepository;
    private final BenefitService benefitService;

    @GetMapping("/all")
    public List<Benefit> getAll() {
        return benefitRepository.findAll();
    }



    @GetMapping("/employee/{id}")
    public List<Benefit> getByEmployee(@PathVariable String id) {
        return benefitRepository.findByEmployeeId(id);
    }


    @PostMapping("/generate-monthly")
    public ResponseEntity<String> generateMonthlyBenefits() {
        benefitService.generateMonthlyBenefits();
        return ResponseEntity.ok("Benefits generated successfully!");
    }
}
