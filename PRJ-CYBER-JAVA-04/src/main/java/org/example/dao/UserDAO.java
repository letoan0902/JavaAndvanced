package org.example.dao;

import org.example.model.User;
import java.util.List;

// Interface DAO cho User - ap dung DIP (SOLID)
public interface UserDAO {
    User findByUsername(String username);
    User findById(int id);
    List<User> findAll();
    boolean insert(User user);
    boolean update(User user);
    boolean updateBalance(int userId, double newBalance);
}
