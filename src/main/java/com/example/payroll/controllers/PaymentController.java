package com.example.payroll.controllers;

import com.example.payroll.model.Payment;
import com.example.payroll.services.interfaces.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    @PostMapping
    public Payment createPayment(@RequestBody Payment payment) {
        return paymentService.createPayment(payment);
    }

    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{id}")
    public Payment getPaymentById(@PathVariable String id) {
        return paymentService.getPaymentById(id);
    }

    @GetMapping("/employee/{employeeId}")
    public List<Payment> getPaymentsByEmployee(@PathVariable String employeeId) {
        return paymentService.getPaymentsByEmployeeId(employeeId);
    }

    @PutMapping("/{id}")
    public Payment updatePayment(@PathVariable String id, @RequestBody Payment payment) {
        return paymentService.updatePayment(id, payment);
    }

    @DeleteMapping("/{id}")
    public void deletePayment(@PathVariable String id) {
        paymentService.deletePayment(id);
    }

    @GetMapping("/calendar")
    public List<Payment> getPaymentsForCalendar(@RequestParam("startDate") Date startDate,
                                                @RequestParam("endDate") Date endDate) {
        return paymentService.getPaymentsByDateRange(startDate, endDate);
    }




}
