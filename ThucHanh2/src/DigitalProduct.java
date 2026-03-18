public class DigitalProduct extends Product {
    private double sizeMb;

    public DigitalProduct(String id, String name, double price, double sizeMb) {
        super(id, name, price);
        this.sizeMb = sizeMb;
    }

    public double getSizeMb() {
        return sizeMb;
    }

    public void setSizeMb(double sizeMb) {
        this.sizeMb = sizeMb;
    }

    @Override
    public void displayInfo() {
        System.out.println("[Digital] id=" + id + ", name=" + name + ", price=" + price + ", size=" + sizeMb + "MB");
    }
}
