package org.example;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public final class TransferService {

    public List<Account> transfer(Connection conn, String fromId, String toId, BigDecimal amount) throws SQLException {
        String sqlCheckSender = "SELECT Balance FROM Accounts WHERE AccountId = ? FOR UPDATE";
        String sqlCheckReceiver = "SELECT AccountId FROM Accounts WHERE AccountId = ? FOR UPDATE";
        String sqlCallUpdate = "{call sp_UpdateBalance(?, ?)}";

        if (isBlank(fromId) || isBlank(toId)) {
            throw new IllegalArgumentException("fromId/toId must not be blank");
        }
        if (fromId.trim().equalsIgnoreCase(toId.trim())) {
            throw new IllegalArgumentException("fromId and toId must be different");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("amount must be > 0");
        }

        boolean originalAutoCommit = conn.getAutoCommit();
        conn.setAutoCommit(false);

        try {
            BigDecimal senderBalance;
            try (PreparedStatement ps = conn.prepareStatement(sqlCheckSender)) {
                ps.setString(1, fromId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new IllegalStateException("Sender account not found: " + fromId);
                    }
                    senderBalance = rs.getBigDecimal(1);
                }
            }

            if (senderBalance == null) {
                throw new IllegalStateException("Sender balance is NULL for: " + fromId);
            }
            if (senderBalance.compareTo(amount) < 0) {
                throw new IllegalStateException("Insufficient balance. Current=" + senderBalance + ", required=" + amount);
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlCheckReceiver)) {
                ps.setString(1, toId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new IllegalStateException("Receiver account not found: " + toId);
                    }
                }
            }

            try (CallableStatement cs = conn.prepareCall(sqlCallUpdate)) {
                cs.setString(1, fromId);
                cs.setBigDecimal(2, amount.negate());
                int updated1 = cs.executeUpdate();
                if (updated1 == 0) {
                    throw new SQLException("Debit failed; no rows affected for: " + fromId);
                }

                cs.setString(1, toId);
                cs.setBigDecimal(2, amount);
                int updated2 = cs.executeUpdate();
                if (updated2 == 0) {
                    throw new SQLException("Credit failed; no rows affected for: " + toId);
                }
            }

            conn.commit();
            return fetchAccounts(conn, fromId, toId);
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(originalAutoCommit);
        }
    }

    public List<Account> fetchAccounts(Connection conn, String id1, String id2) throws SQLException {
        String sqlSelect = "SELECT AccountId, FullName, Balance FROM Accounts WHERE AccountId IN (?, ?) ORDER BY AccountId";
        List<Account> accounts = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sqlSelect)) {
            ps.setString(1, id1);
            ps.setString(2, id2);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    accounts.add(new Account(
                            rs.getString("AccountId"),
                            rs.getString("FullName"),
                            rs.getBigDecimal("Balance")
                    ));
                }
            }
        }
        return accounts;
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}

