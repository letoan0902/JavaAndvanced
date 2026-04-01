package org.example.service;

import org.example.model.User;

// Interface cho AuthService - ap dung DIP (SOLID)
public interface IAuthService {
    boolean register(String username, String password, String fullName, String phone);
    User login(String username, String password);
}
