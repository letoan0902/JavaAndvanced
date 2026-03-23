package org.example.bai5;

import org.example.bai5.dao.DoctorDao;
import org.example.bai5.presentation.ConsoleMenu;
import org.example.bai5.service.DoctorService;

/**
 * Entry point cho Bài 5.
 */
public class Bai5App {
    public static void main(String[] args) {
        DoctorDao dao = new DoctorDao();
        DoctorService service = new DoctorService(dao);
        ConsoleMenu menu = new ConsoleMenu(service);
        menu.start();
    }
}

