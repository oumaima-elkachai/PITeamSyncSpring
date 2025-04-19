package com.example.payroll.services.interfaces;

import com.example.payroll.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionService {

    List<Transaction> getAllTransactions();

    Optional<Transaction> getTransactionById(String id);  // Utilisation de String pour l'ID

    Transaction saveTransaction(Transaction transaction);

    void deleteTransaction(String id);  // Utilisation de String pour l'ID
}
