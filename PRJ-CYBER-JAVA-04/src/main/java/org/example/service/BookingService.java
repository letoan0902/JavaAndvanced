package org.example.service;

import org.example.dao.BookingDAO;
import org.example.dao.PCDAO;
import org.example.dao.UserDAO;
import org.example.dao.CategoryDAO;
import org.example.dao.impl.BookingDAOImpl;
import org.example.dao.impl.PCDAOImpl;
import org.example.dao.impl.UserDAOImpl;
import org.example.dao.impl.CategoryDAOImpl;
import org.example.model.Booking;
import org.example.model.PC;
import org.example.model.User;
import org.example.model.Category;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class BookingService implements IBookingService {

    private final BookingDAO bookingDAO;
    private final PCDAO pcDAO;
    private final UserDAO userDAO;
    private final CategoryDAO categoryDAO;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public BookingService() {
        this.bookingDAO = new BookingDAOImpl();
        this.pcDAO = new PCDAOImpl();
        this.userDAO = new UserDAOImpl();
        this.categoryDAO = new CategoryDAOImpl();
    }

    @Override
    public List<PC> getAvailablePCs() {
        return pcDAO.findAvailable();
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryDAO.findAll();
    }

    @Override
    public List<PC> getPCsByCategory(int categoryId) {
        return pcDAO.findByCategoryId(categoryId);
    }

    @Override
    public List<Booking> getBookingsByUser(int userId) {
        return bookingDAO.findByUserId(userId);
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingDAO.findAll();
    }

    @Override
    public String bookPC(int userId, int pcId, String startTimeStr, int minutes) {
        // 1. Validate ph\u00fat thu\u00ea
        if (minutes <= 0) {
            return "S\u1ed1 ph\u00fat thu\u00ea ph\u1ea3i l\u1edbn h\u01a1n 0!";
        }

        // 2. Parse th\u1eddi gian b\u1eaft \u0111\u1ea7u (dd-MM-yyyy HH:mm)
        LocalDateTime startTime;
        try {
            startTime = LocalDateTime.parse(startTimeStr, FORMATTER);
        } catch (DateTimeParseException e) {
            return "\u0110\u1ecbnh d\u1ea1ng th\u1eddi gian kh\u00f4ng h\u1ee3p l\u1ec7! (dd-MM-yyyy HH:mm)";
        }

        // 3. Validate th\u1eddi gian b\u1eaft \u0111\u1ea7u ph\u1ea3i l\u1edbn h\u01a1n hi\u1ec7n t\u1ea1i
        if (startTime.isBefore(LocalDateTime.now())) {
            return "Th\u1eddi gian \u0111\u1eb7t ph\u1ea3i l\u1edbn h\u01a1n th\u1eddi gian hi\u1ec7n t\u1ea1i!";
        }

        // 4. T\u00ednh th\u1eddi gian k\u1ebft th\u00fac
        LocalDateTime endTime = startTime.plusMinutes(minutes);

        // 5. Ki\u1ec3m tra m\u00e1y t\u1ed3n t\u1ea1i
        PC pc = pcDAO.findById(pcId);
        if (pc == null) {
            return "Kh\u00f4ng t\u00ecm th\u1ea5y m\u00e1y tr\u1ea1m v\u1edbi ID = " + pcId;
        }

        // 6. Ki\u1ec3m tra tr\u00f9ng l\u1ecbch
        String startStr = startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        String endStr = endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        if (bookingDAO.isOverlapping(pcId, startStr, endStr, 0)) {
            return "M\u00e1y " + pc.getPcNumber() + " \u0111\u00e3 c\u00f3 ng\u01b0\u1eddi \u0111\u1eb7t trong khung gi\u1edd n\u00e0y!";
        }

        // 7. T\u00ednh ti\u1ec1n
        double hours = minutes / 60.0;
        double totalPrice = hours * pc.getPricePerHour();

        // 8. Ki\u1ec3m tra s\u1ed1 d\u01b0
        User user = userDAO.findById(userId);
        if (user == null) {
            return "Kh\u00f4ng t\u00ecm th\u1ea5y t\u00e0i kho\u1ea3n!";
        }
        if (user.getBalance() < totalPrice) {
            return String.format("S\u1ed1 d\u01b0 kh\u00f4ng \u0111\u1ee7! C\u1ea7n %,.0f VND, hi\u1ec7n c\u00f3 %,.0f VND. Vui l\u00f2ng n\u1ea1p th\u00eam ti\u1ec1n.",
                    totalPrice, user.getBalance());
        }

        // 9. T\u1ea1o booking (ch\u01b0a tr\u1eeb ti\u1ec1n, \u0111\u1ee3i nh\u00e2n vi\u00ean x\u00e1c nh\u1eadn m\u1edbi tr\u1eeb)
        Booking booking = new Booking(userId, pcId, startTime, endTime, totalPrice);
        boolean success = bookingDAO.insert(booking);

        if (success) {
            return String.format("\u0110\u1eb7t m\u00e1y %s th\u00e0nh c\u00f4ng! Th\u1eddi gian: %d ph\u00fat (%.1f gi\u1edd), T\u1ed5ng ti\u1ec1n: %,.0f VND. Ti\u1ec1n s\u1ebd \u0111\u01b0\u1ee3c tr\u1eeb khi nh\u00e2n vi\u00ean x\u00e1c nh\u1eadn.",
                    pc.getPcNumber(), minutes, hours, totalPrice);
        }
        return "\u0110\u1eb7t m\u00e1y th\u1ea5t b\u1ea1i! Vui l\u00f2ng th\u1eed l\u1ea1i.";
    }

    @Override
    public boolean cancelBooking(int bookingId, int userId) {
        Booking booking = bookingDAO.findById(bookingId);
        if (booking == null) return false;
        if (booking.getUserId() != userId) return false;
        if (!booking.getStatus().equals("PENDING") && !booking.getStatus().equals("CONFIRMED")) {
            return false;
        }
        return bookingDAO.updateStatus(bookingId, "CANCELLED");
    }
}
