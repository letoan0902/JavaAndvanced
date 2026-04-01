package org.example.model;

/**
 * PC - Entity đại diện cho máy trạm trong phòng Cyber
 *
 * Trạng thái: AVAILABLE / IN_USE / MAINTENANCE
 */
public class PC {

    private int id;
    private String pcNumber;
    private int categoryId;
    private String categoryName;
    private String configInfo;
    private double pricePerHour;
    private String status;


    public PC() {
    }

    public PC(int id, String pcNumber, int categoryId, String categoryName,
            String configInfo, double pricePerHour, String status) {
        this.id = id;
        this.pcNumber = pcNumber;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.configInfo = configInfo;
        this.pricePerHour = pricePerHour;
        this.status = status;
    }


    public PC(String pcNumber, int categoryId, String configInfo, double pricePerHour) {
        this.pcNumber = pcNumber;
        this.categoryId = categoryId;
        this.configInfo = configInfo;
        this.pricePerHour = pricePerHour;
        this.status = "AVAILABLE";
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPcNumber() {
        return pcNumber;
    }

    public void setPcNumber(String pcNumber) {
        this.pcNumber = pcNumber;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getConfigInfo() {
        return configInfo;
    }

    public void setConfigInfo(String configInfo) {
        this.configInfo = configInfo;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("PC{id=%d, pcNumber='%s', category='%s', price=%.0f/h, status='%s'}",
                id, pcNumber, categoryName, pricePerHour, status);
    }
}
