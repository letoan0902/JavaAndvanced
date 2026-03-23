package org.example.bai5.model;

/**
 * Model: Doctor (bác sĩ trực ca)
 */
public class Doctor {
    private String id;
    private String fullName;
    private String specialty;

    public Doctor() {
    }

    public Doctor(String id, String fullName, String specialty) {
        this.id = id;
        this.fullName = fullName;
        this.specialty = specialty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", specialty='" + specialty + '\'' +
                '}';
    }
}

