package bai3;

public class CODPayment implements CODPayable {
    @Override
    public void processCOD(long amount) {
        System.out.println("Xử lý thanh toán COD: " + amount + " - Thành công");
    }

    @Override
    public String getName() {
        return "COD";
    }
}
