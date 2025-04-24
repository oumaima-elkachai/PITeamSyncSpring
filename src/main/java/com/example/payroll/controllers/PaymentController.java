package com.example.payroll.controllers;

import com.example.payroll.model.Payment;
import com.example.payroll.repository.PaymentRepository;
import com.example.payroll.services.interfaces.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    private PaymentRepository paymentRepository;
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

    @PostMapping("/predict-status")
    public ResponseEntity<String> predictPaymentStatus(@RequestBody Map<String, Object> data) {
        String flaskUrl = "http://localhost:5000/predict";

        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(data, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(flaskUrl, request, String.class);

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la prédiction : " + e.getMessage());
        }
    }

    @GetMapping("/payments/monthly")
    public Map<String, Double> getPaymentsGroupedByMonth() {
        List<Payment> payments = paymentRepository.findAll();
        Map<String, Double> paymentsByMonth = new HashMap<>();

        for (Payment payment : payments) {
            String month = new SimpleDateFormat("MMMM").format(payment.getPaymentDate());
            paymentsByMonth.put(month, paymentsByMonth.getOrDefault(month, 0.0) + payment.getAmount());
        }

        return paymentsByMonth;
    }





}
