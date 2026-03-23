package org.example.bai5.service;

import org.example.bai5.dao.DoctorDao;
import org.example.bai5.model.Doctor;

import java.sql.SQLException;
import java.util.List;

/**
 * Business layer: validate input + xử lý nghiệp vụ.
 */
public class DoctorService {

    // Có thể chỉnh theo độ rộng cột thật trong DB
    public static final int MAX_ID_LENGTH = 20;
    public static final int MAX_NAME_LENGTH = 100;
    public static final int MAX_SPECIALTY_LENGTH = 50;

    private final DoctorDao doctorDao;

    public DoctorService(DoctorDao doctorDao) {
        this.doctorDao = doctorDao;
    }

    public List<Doctor> getAllDoctors() throws SQLException {
        return doctorDao.findAll();
    }

    public int addDoctor(String id, String fullName, String specialty) throws ValidationException, SQLException {
        id = safeTrim(id);
        fullName = safeTrim(fullName);
        specialty = safeTrim(specialty);

        if (id.isEmpty()) throw new ValidationException("Mã bác sĩ không được để trống.");
        if (fullName.isEmpty()) throw new ValidationException("Họ tên không được để trống.");
        if (specialty.isEmpty()) throw new ValidationException("Chuyên khoa không được để trống.");

        if (id.length() > MAX_ID_LENGTH) {
            throw new ValidationException("Mã bác sĩ quá dài (tối đa " + MAX_ID_LENGTH + " ký tự).");
        }
        if (fullName.length() > MAX_NAME_LENGTH) {
            throw new ValidationException("Họ tên quá dài (tối đa " + MAX_NAME_LENGTH + " ký tự).");
        }
        if (specialty.length() > MAX_SPECIALTY_LENGTH) {
            throw new ValidationException("Chuyên khoa quá dài (tối đa " + MAX_SPECIALTY_LENGTH + " ký tự).");
        }

        Doctor d = new Doctor(id, fullName, specialty);
        return doctorDao.insert(d);
    }

    public List<DoctorDao.SpecialtyStat> getSpecialtyStats() throws SQLException {
        return doctorDao.countBySpecialty();
    }

    private static String safeTrim(String s) {
        return s == null ? "" : s.trim();
    }

    public static class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
    }
}

