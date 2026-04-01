package org.example.dao;

import org.example.model.Category;
import java.util.List;

// Interface DAO cho Category (khu vuc phong may)
public interface CategoryDAO {
    List<Category> findAll();
    Category findById(int id);
}
