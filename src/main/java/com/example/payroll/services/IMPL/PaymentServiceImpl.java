package com.example.payroll.services.IMPL;

import com.example.payroll.model.Payment;
import com.example.payroll.model.Payroll;
import com.example.payroll.repository.PaymentRepository;
import com.example.payroll.repository.PayrollRepository;
import com.example.payroll.services.interfaces.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PayrollRepository payrollRepository;

    @Override
    public Payment createPayment(Payment payment) {
        // Récupérer le dernier payroll pour l'employé
        Payroll payroll = payrollRepository.findTopByEmployeeIdOrderByPayDateDesc(payment.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Aucun payroll trouvé pour cet employé"));

        // Définir le payrollId dans le paiement
        payment.setPayrollId(payroll.getId());

        // Calculer le montant total à partir du salary + bonus
        Double totalAmount = payroll.getSalary() + payroll.getBonus();
        payment.setAmount(totalAmount);

        // Définir d'autres détails du paiement
        if (payment.getPaymentDate() == null) {
            payment.setPaymentDate(payroll.getPayDate());
        }

        // Générer un numéro de référence
        payment.generateReferenceNumber(payroll.getPayDate());

        // Sauvegarder et retourner le paiement
        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPaymentById(String id) {
        return paymentRepository.findById(id).orElse(null);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public List<Payment> getPaymentsByEmployeeId(String employeeId) {
        return paymentRepository.findByEmployeeId(employeeId);
    }

    @Transactional
    @Override
    public Payment updatePayment(String paymentId, Payment payment) {
        // Récupérer le paiement existant
        Payment existingPayment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé"));

        // Récupérer le dernier payroll pour l'employé
        Payroll payroll = payrollRepository.findTopByEmployeeIdOrderByPayDateDesc(payment.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Aucun payroll trouvé pour cet employé"));

        // Mettre à jour le payrollId dans le paiement existant
        existingPayment.setPayrollId(payroll.getId());

        // Calculer le nouveau montant total
        Double totalAmount = payroll.getSalary() + payroll.getBonus();
        existingPayment.setAmount(totalAmount);

        // Mettre à jour les autres champs du paiement
        existingPayment.setPaymentMethod(payment.getPaymentMethod());
        existingPayment.setStatus(payment.getStatus());
        existingPayment.setDescription(payment.getDescription());
        existingPayment.setRecurring(payment.isRecurring());
        existingPayment.setRecurrenceFrequency(payment.getRecurrenceFrequency());

        if (payment.getPaymentDate() == null) {
            existingPayment.setPaymentDate(payroll.getPayDate());
        } else {
            existingPayment.setPaymentDate(payment.getPaymentDate());
        }

        // Générer un nouveau numéro de référence
        existingPayment.generateReferenceNumber(payroll.getPayDate());

        // Sauvegarder et retourner le paiement mis à jour
        return paymentRepository.save(existingPayment);
    }



    @Override
    public void deletePayment(String id) {
        paymentRepository.deleteById(id);
    }

    public void updatePaymentAmountByPayroll(Payroll payroll) {
        List<Payment> payments = paymentRepository.findByPayrollId(payroll.getId());
        for (Payment payment : payments) {
            payment.setAmount(payroll.getSalary() + payroll.getBonus());
            payment.setPaymentDate(payroll.getPayDate()); // facultatif
            payment.generateReferenceNumber(payroll.getPayDate());
            paymentRepository.save(payment);
        }
    }

    @Override
    public List<Payment> getPaymentsByDateRange(Date startDate, Date endDate) {
        return paymentRepository.findByPaymentDateBetween(startDate, endDate);
    }




}
