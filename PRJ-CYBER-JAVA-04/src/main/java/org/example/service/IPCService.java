package org.example.service;

import org.example.model.PC;
import org.example.model.Category;
import java.util.List;

// Interface cho PCService - ap dung DIP (SOLID)
public interface IPCService {
    List<PC> getAllPCs();
    List<PC> getAvailablePCs();
    List<PC> getPCsByCategory(int categoryId);
    PC getPCById(int id);
    List<Category> getAllCategories();
    boolean addPC(PC pc);
    boolean updatePC(PC pc);
    boolean deletePC(int id);
    boolean updatePCStatus(int id, String status);
}
