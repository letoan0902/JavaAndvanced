package org.example.service;

import org.example.model.Food;
import java.util.List;

// Interface cho FoodService - ap dung DIP (SOLID)
public interface IFoodService {
    List<Food> getAllFoods();
    List<Food> getAvailableFoods();
    Food getFoodById(int id);
    boolean addFood(Food food);
    boolean updateFood(Food food);
    boolean deleteFood(int id);
    boolean updateStock(int id, int newQuantity);
}
