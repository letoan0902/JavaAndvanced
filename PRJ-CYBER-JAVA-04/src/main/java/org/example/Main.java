package org.example;

import org.example.presentation.MainMenu;
import org.example.util.DBConnection;

// Main - Entry point cua ung dung Cyber Gaming Management System
public class Main {

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("       CYBER GAMING & F&B MANAGEMENT SYSTEM");
        System.out.println("       PRJ-CYBER-JAVA-04");
        System.out.println("============================================================");
        System.out.println();

        // Ket noi Database
        System.out.println("Dang ket noi Database...");
        DBConnection dbConnection = DBConnection.getInstance();

        if (dbConnection.getConnection() != null) {
            System.out.println("[OK] He thong san sang hoat dong!");
            System.out.println();

            // Khoi dong menu chinh
            MainMenu mainMenu = new MainMenu();
            mainMenu.show();
        } else {
            System.out.println("[LOI] Khong the ket noi Database. Vui long kiem tra:");
            System.out.println("   1. MySQL da khoi dong chua?");
            System.out.println("   2. Database 'cyber_gaming_db' da tao chua?");
            System.out.println("   3. Thong tin ket noi trong DBConnection.java dung chua?");
        }

        // Dong ket noi
        dbConnection.closeConnection();
    }
}