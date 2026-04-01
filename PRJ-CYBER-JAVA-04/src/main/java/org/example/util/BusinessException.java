package org.example.util;

// Custom Exception cho nghiep vu Cyber Gaming
// Ap dung: Exception Handling (Session 1-3 bai giang)
public class BusinessException extends Exception {

    private final String errorCode;

    public BusinessException(String message) {
        super(message);
        this.errorCode = "GENERAL";
    }

    public BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "GENERAL";
    }

    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", errorCode, getMessage());
    }
}
