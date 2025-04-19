package com.example.payroll.services.interfaces;

import com.example.payroll.model.Payment;

import java.util.Date;
import  java.util.List;

public interface PaymentService {
    Payment createPayment(Payment payment);
    Payment getPaymentById(String id);
    List<Payment> getAllPayments();
    List<Payment> getPaymentsByEmployeeId(String employeeId);
    Payment updatePayment(String id, Payment updatedPayment);
    void deletePayment(String id);
    List<Payment> getPaymentsByDateRange(Date startDate, Date endDate);



}

