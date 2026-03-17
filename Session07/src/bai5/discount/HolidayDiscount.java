package bai5.discount;

public class HolidayDiscount extends PercentageDiscount {
    public HolidayDiscount() {
        super(15);
    }

    @Override
    public String getName() {
        return "Holiday " + super.getName();
    }
}
