interface UserActions {
    default void logActivity(String activity) {
        System.out.println("UserActions: " + activity);
    }
}

interface AdminActions {
    default void logActivity(String activity) {
        System.out.println("AdminActions: " + activity);
    }
}

class SuperAdmin implements UserActions, AdminActions {
    @Override
    public void logActivity(String activity) {
        UserActions.super.logActivity(activity);
        AdminActions.super.logActivity(activity);
        System.out.println("SuperAdmin: " + activity);
    }
}

public class Bai5 {
    public static void main(String[] args) {
        SuperAdmin superAdmin = new SuperAdmin();
        superAdmin.logActivity("Cập nhật quyền người dùng");
    }
}
