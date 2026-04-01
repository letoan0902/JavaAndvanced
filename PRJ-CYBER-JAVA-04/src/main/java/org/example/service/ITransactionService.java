package org.example.service;

import org.example.model.Transaction;
import java.util.List;

// Interface cho TransactionService (vi dien tu) - ap dung DIP (SOLID)
public interface ITransactionService {
    String deposit(int userId, double amount);
    String payBooking(int userId, double amount, String description);
    String payOrder(int userId, double amount, String description);
    String refund(int userId, double amount, String description);
    List<Transaction> getTransactionsByUser(int userId);
    List<Transaction> getAllTransactions();
    double getBalance(int userId);
}
