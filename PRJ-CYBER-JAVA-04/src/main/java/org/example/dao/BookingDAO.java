package org.example.dao;

import org.example.model.Booking;
import java.util.List;

// Interface DAO cho Booking (dat may tram)
public interface BookingDAO {
    List<Booking> findByUserId(int userId);
    List<Booking> findByPcId(int pcId);
    List<Booking> findAll();
    Booking findById(int id);
    boolean insert(Booking booking);
    boolean updateStatus(int id, String status);
    // Kiem tra may co bi trung lich khong
    boolean isOverlapping(int pcId, String startTime, String endTime, int excludeBookingId);
}
