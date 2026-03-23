package org.example.bai5.dao;

import org.example.bai1.DBContext;
import org.example.bai5.model.Doctor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO layer: thao tác DB với bảng Doctors.
 */
public class DoctorDao {

    // NOTE: Tên bảng/cột có thể cần chỉnh theo DB thực tế.
    private static final String SQL_SELECT_ALL =
            "SELECT doctor_id, full_name, specialty FROM Doctors ORDER BY doctor_id";

    private static final String SQL_INSERT =
            "INSERT INTO Doctors (doctor_id, full_name, specialty) VALUES (?, ?, ?)";

    private static final String SQL_GROUP_BY_SPECIALTY =
            "SELECT specialty, COUNT(*) AS total FROM Doctors GROUP BY specialty ORDER BY total DESC";

    public List<Doctor> findAll() throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Doctor> doctors = new ArrayList<>();

        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(SQL_SELECT_ALL);
            rs = ps.executeQuery();

            while (rs.next()) {
                Doctor d = new Doctor();
                d.setId(rs.getString("doctor_id"));
                d.setFullName(rs.getString("full_name"));
                d.setSpecialty(rs.getString("specialty"));
                doctors.add(d);
            }
            return doctors;
        } finally {
            DBContext.closeQuietly(rs);
            DBContext.closeQuietly(ps);
            DBContext.closeQuietly(conn);
        }
    }

    public int insert(Doctor doctor) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(SQL_INSERT);
            ps.setString(1, doctor.getId());
            ps.setString(2, doctor.getFullName());
            ps.setString(3, doctor.getSpecialty());

            return ps.executeUpdate();
        } finally {
            DBContext.closeQuietly(ps);
            DBContext.closeQuietly(conn);
        }
    }

    public List<SpecialtyStat> countBySpecialty() throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<SpecialtyStat> stats = new ArrayList<>();

        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(SQL_GROUP_BY_SPECIALTY);
            rs = ps.executeQuery();

            while (rs.next()) {
                String specialty = rs.getString("specialty");
                int total = rs.getInt("total");
                stats.add(new SpecialtyStat(specialty, total));
            }

            return stats;
        } finally {
            DBContext.closeQuietly(rs);
            DBContext.closeQuietly(ps);
            DBContext.closeQuietly(conn);
        }
    }

    public static final class SpecialtyStat {
        private final String specialty;
        private final int total;

        public SpecialtyStat(String specialty, int total) {
            this.specialty = specialty;
            this.total = total;
        }

        public String getSpecialty() {
            return specialty;
        }

        public int getTotal() {
            return total;
        }
    }
}

