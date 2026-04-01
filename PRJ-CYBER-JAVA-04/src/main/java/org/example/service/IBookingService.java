package org.example.service;

import org.example.model.Booking;
import org.example.model.PC;
import org.example.model.Category;
import java.util.List;

// Interface cho BookingService - ap dung DIP (SOLID)
public interface IBookingService {
    List<PC> getAvailablePCs();
    List<Category> getAllCategories();
    List<PC> getPCsByCategory(int categoryId);
    List<Booking> getBookingsByUser(int userId);
    List<Booking> getAllBookings();
    String bookPC(int userId, int pcId, String startTimeStr, int minutes);
    boolean cancelBooking(int bookingId, int userId);
}
