package org.example.service;

import org.example.dao.PCDAO;
import org.example.dao.CategoryDAO;
import org.example.dao.impl.PCDAOImpl;
import org.example.dao.impl.CategoryDAOImpl;
import org.example.model.PC;
import org.example.model.Category;

import java.util.List;

// PCService - Xu ly logic nghiep vu cho may tram
public class PCService implements IPCService {

    private final PCDAO pcDAO;
    private final CategoryDAO categoryDAO;

    public PCService() {
        this.pcDAO = new PCDAOImpl();
        this.categoryDAO = new CategoryDAOImpl();
    }

    public List<PC> getAllPCs() {
        return pcDAO.findAll();
    }

    public List<PC> getAvailablePCs() {
        return pcDAO.findAvailable();
    }

    public List<PC> getPCsByCategory(int categoryId) {
        return pcDAO.findByCategoryId(categoryId);
    }

    public PC getPCById(int id) {
        return pcDAO.findById(id);
    }

    public List<Category> getAllCategories() {
        return categoryDAO.findAll();
    }

    // Them may moi - kiem tra trung pc_number
    public boolean addPC(PC pc) {
        // Kiem tra category co ton tai khong
        Category cat = categoryDAO.findById(pc.getCategoryId());
        if (cat == null) {
            return false;
        }
        return pcDAO.insert(pc);
    }

    // Cap nhat thong tin may
    public boolean updatePC(PC pc) {
        // Kiem tra may co ton tai khong
        if (pcDAO.findById(pc.getId()) == null) {
            return false;
        }
        return pcDAO.update(pc);
    }

    // Xoa may
    public boolean deletePC(int id) {
        if (pcDAO.findById(id) == null) {
            return false;
        }
        return pcDAO.delete(id);
    }

    // Cap nhat trang thai may
    public boolean updatePCStatus(int id, String status) {
        return pcDAO.updateStatus(id, status);
    }
}
