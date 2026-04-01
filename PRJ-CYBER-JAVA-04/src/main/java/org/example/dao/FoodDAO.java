package org.example.dao;

import org.example.model.Food;
import java.util.List;

// Interface DAO cho Food (menu F&B)
public interface FoodDAO {
    List<Food> findAll();
    List<Food> findAvailable();
    Food findById(int id);
    boolean insert(Food food);
    boolean update(Food food);
    boolean delete(int id);
    boolean updateStock(int id, int newQuantity);
}
