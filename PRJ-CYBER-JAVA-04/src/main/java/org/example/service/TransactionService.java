package org.example.service;

import org.example.dao.TransactionDAO;
import org.example.dao.UserDAO;
import org.example.dao.impl.TransactionDAOImpl;
import org.example.dao.impl.UserDAOImpl;
import org.example.model.Transaction;
import org.example.model.User;

import java.util.List;

// TransactionService - Vi dien tu noi bo
public class TransactionService implements ITransactionService {

    private final TransactionDAO transactionDAO;
    private final UserDAO userDAO;

    public TransactionService() {
        this.transactionDAO = new TransactionDAOImpl();
        this.userDAO = new UserDAOImpl();
    }

    // Nap tien vao vi
    @Override
    public String deposit(int userId, double amount) {
        if (amount <= 0) {
            return "Số tiền nạp phải lớn hơn 0!";
        }

        User user = userDAO.findById(userId);
        if (user == null) {
            return "Không tìm thấy tài khoản!";
        }

        double newBalance = user.getBalance() + amount;
        userDAO.updateBalance(userId, newBalance);

        Transaction t = new Transaction(userId, "DEPOSIT", amount, newBalance,
                "Nạp tiền vào ví: " + String.format("%,.0f VND", amount));
        transactionDAO.insert(t);

        return String.format("Nạp %,.0f VND thành công! Số dư mới: %,.0f VND", amount, newBalance);
    }

    // Thanh toan booking (tru tien)
    @Override
    public String payBooking(int userId, double amount, String description) {
        User user = userDAO.findById(userId);
        if (user == null) {
            return "Không tìm thấy tài khoản!";
        }
        if (user.getBalance() < amount) {
            return String.format("Số dư không đủ! Cần %,.0f VND, hiện có %,.0f VND",
                    amount, user.getBalance());
        }

        double newBalance = user.getBalance() - amount;
        userDAO.updateBalance(userId, newBalance);

        Transaction t = new Transaction(userId, "BOOKING_PAYMENT", amount, newBalance, description);
        transactionDAO.insert(t);

        return String.format("Thanh toán %,.0f VND thành công! Số dư còn: %,.0f VND", amount, newBalance);
    }

    // Thanh toan don hang F&B (tru tien)
    @Override
    public String payOrder(int userId, double amount, String description) {
        User user = userDAO.findById(userId);
        if (user == null) {
            return "Không tìm thấy tài khoản!";
        }
        if (user.getBalance() < amount) {
            return String.format("Số dư không đủ! Cần %,.0f VND, hiện có %,.0f VND",
                    amount, user.getBalance());
        }

        double newBalance = user.getBalance() - amount;
        userDAO.updateBalance(userId, newBalance);

        Transaction t = new Transaction(userId, "ORDER_PAYMENT", amount, newBalance, description);
        transactionDAO.insert(t);

        return String.format("Thanh toán %,.0f VND thành công! Số dư còn: %,.0f VND", amount, newBalance);
    }

    // Hoan tien (khi huy booking/order)
    @Override
    public String refund(int userId, double amount, String description) {
        User user = userDAO.findById(userId);
        if (user == null) {
            return "Không tìm thấy tài khoản!";
        }

        double newBalance = user.getBalance() + amount;
        userDAO.updateBalance(userId, newBalance);

        Transaction t = new Transaction(userId, "REFUND", amount, newBalance, description);
        transactionDAO.insert(t);

        return String.format("Hoàn %,.0f VND thành công! Số dư mới: %,.0f VND", amount, newBalance);
    }

    @Override
    public List<Transaction> getTransactionsByUser(int userId) {
        return transactionDAO.findByUserId(userId);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionDAO.findAll();
    }

    @Override
    public double getBalance(int userId) {
        User user = userDAO.findById(userId);
        return user != null ? user.getBalance() : 0;
    }
}
