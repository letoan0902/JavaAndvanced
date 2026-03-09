public class Bai3 {
    public static void main(String[] args) {
        UserBai3 user = new UserBai3();
        try {
            user.setAge(-5);
            System.out.println("Tuổi hợp lệ: " + user.getAge());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}

class UserBai3 {
    private int age;

    public void setAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Tuổi không thể âm!");
        }
        this.age = age;
    }

    public int getAge() {
        return age;
    }
}
