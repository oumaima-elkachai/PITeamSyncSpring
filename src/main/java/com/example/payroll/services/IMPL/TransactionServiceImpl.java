package com.example.payroll.services.IMPL;

import com.example.payroll.model.Payroll;
import com.example.payroll.model.ProjectBudget;
import com.example.payroll.model.Transaction;
import com.example.payroll.repository.PayrollRepository;
import com.example.payroll.repository.ProjectBudgetRepository;
import com.example.payroll.repository.TransactionRepository;
import com.example.payroll.services.interfaces.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PayrollRepository payrollRepository;  // Pour récupérer les informations de Payroll

    @Autowired
    private ProjectBudgetRepository projectBudgetRepository;  // Pour récupérer les informations de ProjectBudget

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Optional<Transaction> getTransactionById(String id) {
        return transactionRepository.findById(id);  // Utilisation de String pour l'ID
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        // Vérifier que les objets Payroll et ProjectBudget sont bien fournis
        if (transaction.getPayroll() == null || transaction.getPayroll().getId() == null) {
            throw new RuntimeException("Payroll ID is required");
        }
        if (transaction.getProjectBudget() == null || transaction.getProjectBudget().getId() == null) {
            throw new RuntimeException("ProjectBudget ID is required");
        }

        // Récupérer les entités Payroll et ProjectBudget depuis la base de données
        Payroll payroll = payrollRepository.findById(transaction.getPayroll().getId())
                .orElseThrow(() -> new RuntimeException("Payroll not found with ID: " + transaction.getPayroll().getId()));

        ProjectBudget projectBudget = projectBudgetRepository.findById(transaction.getProjectBudget().getId())
                .orElseThrow(() -> new RuntimeException("ProjectBudget not found with ID: " + transaction.getProjectBudget().getId()));

        // Associer les entités récupérées à la transaction
        transaction.setPayroll(payroll);
        transaction.setProjectBudget(projectBudget);

        // Persister la transaction
        return transactionRepository.save(transaction);
    }

    @Override
    public void deleteTransaction(String id) {  // Utilisation de String pour l'ID
        transactionRepository.deleteById(id);
    }
}
