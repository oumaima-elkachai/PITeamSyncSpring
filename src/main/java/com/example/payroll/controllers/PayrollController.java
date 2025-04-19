package com.example.payroll.controllers;

import com.example.payroll.model.Payroll;
import com.example.payroll.services.interfaces.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/payrolls")
public class PayrollController {

    @Autowired
    private PayrollService payrollService;


    @GetMapping("/{id}")
    public ResponseEntity<Payroll> getPayrollById(@PathVariable String id) {  // Utilisation de String pour l'ID
        Payroll payroll = payrollService.getPayrollById(id);
        return ResponseEntity.ok(payroll);
    }

    @GetMapping
    public List<Payroll> getAllPayrolls() {
        return payrollService.getAllPayrolls();
    }

    @PostMapping("/savePayroll")
    public ResponseEntity<Payroll> savePayroll(@RequestBody Payroll payroll) {
        // Sauvegarder le payroll via le service
        Payroll savedPayroll = payrollService.savePayroll(payroll);

        // Retourner une réponse avec statut HTTP 201 (Created) et le payroll enregistré
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPayroll);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Payroll> updatePayroll(@PathVariable String id, @RequestBody Payroll payrollDetails) {  // Utilisation de String pour l'ID
        Payroll updatedPayroll = payrollService.updatePayroll(id, payrollDetails);
        return ResponseEntity.ok(updatedPayroll);
    }

    @DeleteMapping("/{id}")
    public void deletePayroll(@PathVariable String id) {  // Utilisation de String pour l'ID
        payrollService.deletePayroll(id);
    }

    @PostMapping("/createPayroll")
    public ResponseEntity<Payroll> createPayroll(@RequestBody Payroll payroll) {
        Payroll savedPayroll = payrollService.savePayroll(payroll);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPayroll);
    }

    // Endpoint pour récupérer les détails de paiement d'un employé par son ID

}
