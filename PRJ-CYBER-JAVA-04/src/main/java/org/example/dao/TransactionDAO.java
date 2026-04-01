package org.example.dao;

import org.example.model.Transaction;
import java.util.List;

// Interface DAO cho Transaction (lich su giao dich vi dien tu)
public interface TransactionDAO {
    List<Transaction> findByUserId(int userId);
    List<Transaction> findAll();
    boolean insert(Transaction transaction);
}
