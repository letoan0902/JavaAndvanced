package org.example.service;

import org.example.dao.FoodDAO;
import org.example.dao.impl.FoodDAOImpl;
import org.example.model.Food;

import java.util.List;

// FoodService - Xu ly logic nghiep vu cho menu F&B
public class FoodService implements IFoodService {

    private final FoodDAO foodDAO;

    public FoodService() {
        this.foodDAO = new FoodDAOImpl();
    }

    public List<Food> getAllFoods() {
        return foodDAO.findAll();
    }

    public List<Food> getAvailableFoods() {
        return foodDAO.findAvailable();
    }

    public Food getFoodById(int id) {
        return foodDAO.findById(id);
    }

    // Them mon moi
    public boolean addFood(Food food) {
        return foodDAO.insert(food);
    }

    // Cap nhat thong tin mon
    public boolean updateFood(Food food) {
        if (foodDAO.findById(food.getId()) == null) {
            return false;
        }
        return foodDAO.update(food);
    }

    // Xoa mon
    public boolean deleteFood(int id) {
        if (foodDAO.findById(id) == null) {
            return false;
        }
        return foodDAO.delete(id);
    }

    // Cap nhat ton kho
    public boolean updateStock(int id, int newQuantity) {
        return foodDAO.updateStock(id, newQuantity);
    }
}
