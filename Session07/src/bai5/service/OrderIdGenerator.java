package bai5.service;

public class OrderIdGenerator {
    private int counter = 0;

    public String nextId() {
        counter++;
        return String.format("ORD%03d", counter);
    }
}
