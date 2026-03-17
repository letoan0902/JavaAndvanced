package bai3;

public class MomoPayment implements EWalletPayable {
    @Override
    public void processEWallet(long amount) {
        System.out.println("Xử lý thanh toán MoMo: " + amount + " - Thành công");
    }

    @Override
    public String getName() {
        return "MoMo";
    }
}
