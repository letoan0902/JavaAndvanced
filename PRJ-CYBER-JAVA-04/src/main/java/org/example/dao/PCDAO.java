package org.example.dao;

import org.example.model.PC;
import java.util.List;

// Interface DAO cho PC (may tram)
public interface PCDAO {
    List<PC> findAll();
    List<PC> findByCategoryId(int categoryId);
    List<PC> findAvailable();
    PC findById(int id);
    boolean insert(PC pc);
    boolean update(PC pc);
    boolean delete(int id);
    boolean updateStatus(int id, String status);
}
