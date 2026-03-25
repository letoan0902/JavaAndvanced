package org.example;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Usage (optional):
        //   gradlew.bat run --args="ACC01 ACC02 500"
        String fromId = args.length > 0 ? args[0] : "ACC01";
        String toId = args.length > 1 ? args[1] : "ACC02";
        BigDecimal amount = args.length > 2 ? new BigDecimal(args[2]) : new BigDecimal("500");

        TransferService service = new TransferService();

        String url = "jdbc:mysql://localhost:3306/banking_db";
        String user = "root";
        String password = "123456";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            List<Account> finalStates = service.transfer(conn, fromId, toId, amount);
            printAccounts(finalStates, fromId, toId, amount);
        } catch (SQLException ex) {
            System.err.println("ERROR: " + ex.getMessage());
            ex.printStackTrace(System.err);
        }
    }

    private static void printAccounts(List<Account> accounts, String fromId, String toId, BigDecimal amount) {
        System.out.println("Transfer OK: " + fromId + " -> " + toId + " | amount=" + amount);
        System.out.println("----------------------------------------------------------------");
        System.out.printf("%-10s | %-20s | %15s%n", "AccountId", "FullName", "Balance");
        System.out.println("----------------------------------------------------------------");
        for (Account a : accounts) {
            System.out.printf("%-10s | %-20s | %15.2f%n", a.accountId(), a.fullName(), a.balance());
        }
        System.out.println("----------------------------------------------------------------");
    }
}