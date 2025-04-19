package com.example.payroll.controllers;

import com.example.payroll.model.Transaction;
import com.example.payroll.services.interfaces.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/{id}")
    public Optional<Transaction> getTransactionById(@PathVariable String id) {  // Utilisation de String pour l'ID
        return transactionService.getTransactionById(id);
    }

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Transaction createdTransaction = transactionService.saveTransaction(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable String id) {  // Utilisation de String pour l'ID
        transactionService.deleteTransaction(id);
    }
}
