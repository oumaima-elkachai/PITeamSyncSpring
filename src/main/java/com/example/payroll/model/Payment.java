package com.example.payroll.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Calendar;
import java.util.Date;

@Document(collection = "payments")
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    private String id;
    private String employeeId;  // Référence à l'employé concerné
    private String payrollId;  // Référence vers le salaire (payroll)
    private Double amount;
    private Date paymentDate;
    private String description;
    private boolean isRecurring;
    private RecurrenceFrequency recurrenceFrequency;
    private PaymentStatus status;
    private PaymentMethod paymentMethod;
    private String referenceNumber;

    // La date prévue de paiement est générée automatiquement en fonction du mois de paiement
    public Date getDueDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.paymentDate);
        calendar.set(Calendar.DAY_OF_MONTH, 7);  // La date prévue est le 7 du mois
        return calendar.getTime();
    }

    // Méthode pour vérifier si le paiement est en retard
    public boolean isPaymentLate() {
        if (this.paymentDate == null) {
            return false;  // Si la date de paiement n'est pas définie, on ne peut pas dire si c'est en retard
        }

        Date dueDate = getDueDate();  // La date prévue
        return this.paymentDate.after(dueDate);  // Si la date réelle est après la date prévue, c'est en retard
    }

    // Elle sert à générer automatiquement un numéro de référence unique pour un paiement
    // en se basant sur :
    // - l'identifiant de l'employé (employeeId)
    // - une date (prioritairement paymentDate, sinon fallbackDate, sinon la date actuelle)
    public void generateReferenceNumber(Date fallbackDate) {
        Date dateToUse = this.paymentDate != null ? this.paymentDate : fallbackDate != null ? fallbackDate : new Date();
        this.referenceNumber = "PAY-" + this.employeeId + "-" + dateToUse.getTime();
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id='" + id + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", amount=" + amount +
                ", paymentDate=" + paymentDate +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", description='" + description + '\'' +
                '}';
    }



    public void setAmount(Double amount) {
            this.amount = amount;
        }
        public void setEmployeeId(String employeeId) {
            this.employeeId = employeeId;
        }

        public void setPaymentDate(Date paymentDate) {
            this.paymentDate = paymentDate;
        }

        public String getId() {
            return id;
        }

        public String getEmployeeId() {
            return employeeId;
        }

        public String getPayrollId() {
            return payrollId;
        }

        public void setPayrollId(String payrollId) {
            this.payrollId = payrollId;
        }

        public Double getAmount() {
            return amount;
        }

        public Date getPaymentDate() {

            return paymentDate;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public boolean isRecurring() {
            return isRecurring;
        }

        public void setRecurring(boolean recurring) {
            isRecurring = recurring;
        }

        public RecurrenceFrequency getRecurrenceFrequency() {
            return recurrenceFrequency;
        }

        public void setRecurrenceFrequency(RecurrenceFrequency recurrenceFrequency) {
            this.recurrenceFrequency = recurrenceFrequency;
        }

        public PaymentStatus getStatus() {
            return status;
        }

        public void setStatus(PaymentStatus status) {
            this.status = status;
        }

        public PaymentMethod getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(PaymentMethod paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public String getReferenceNumber() {
            return referenceNumber;
        }

        public void setReferenceNumber(String referenceNumber) {
            this.referenceNumber = referenceNumber;
        }
    }


