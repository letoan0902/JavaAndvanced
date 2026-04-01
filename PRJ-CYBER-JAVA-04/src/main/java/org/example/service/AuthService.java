package org.example.service;

import org.example.dao.UserDAO;
import org.example.dao.impl.UserDAOImpl;
import org.example.model.User;
import org.example.util.HashUtil;

// AuthService - Xu ly dang ky, dang nhap, phan quyen
public class AuthService implements IAuthService {

    private final UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAOImpl();
    }

    // Dang ky tai khoan moi (mac dinh role = CUSTOMER)
    public boolean register(String username, String password, String fullName, String phone) {
        // Kiem tra username da ton tai chua
        if (userDAO.findByUsername(username) != null) {
            return false; // username da ton tai
        }

        // Hash mat khau truoc khi luu
        String hashedPassword = HashUtil.hashPassword(password);

        User newUser = new User(username, hashedPassword, fullName, phone, "CUSTOMER");
        return userDAO.insert(newUser);
    }

    // Dang nhap - tra ve User neu thanh cong, null neu that bai
    public User login(String username, String password) {
        User user = userDAO.findByUsername(username);
        if (user == null) {
            return null; // username khong ton tai
        }

        // So sanh mat khau da hash
        if (HashUtil.verifyPassword(password, user.getPassword())) {
            return user; // dang nhap thanh cong
        }

        return null; // sai mat khau
    }
}
