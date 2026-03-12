package bai5;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AccessControlTest {

    private final AccessControl accessControl = new AccessControl();
    private User currentUser;

    @AfterEach
    void tearDown() {
        currentUser = null;
    }

    @Test
    void testAdminPermissions() {
        currentUser = new User("admin", Role.ADMIN);
        assertTrue(accessControl.canPerformAction(currentUser, Action.DELETE_USER), "ADMIN should delete user");
        assertTrue(accessControl.canPerformAction(currentUser, Action.LOCK_USER), "ADMIN should lock user");
        assertTrue(accessControl.canPerformAction(currentUser, Action.VIEW_PROFILE), "ADMIN should view profile");
    }

    @Test
    void testModeratorPermissions() {
        currentUser = new User("mod", Role.MODERATOR);
        assertFalse(accessControl.canPerformAction(currentUser, Action.DELETE_USER), "MODERATOR cannot delete user");
        assertTrue(accessControl.canPerformAction(currentUser, Action.LOCK_USER), "MODERATOR should lock user");
        assertTrue(accessControl.canPerformAction(currentUser, Action.VIEW_PROFILE), "MODERATOR should view profile");
    }

    @Test
    void testUserPermissions() {
        currentUser = new User("user", Role.USER);
        assertFalse(accessControl.canPerformAction(currentUser, Action.DELETE_USER), "USER cannot delete user");
        assertFalse(accessControl.canPerformAction(currentUser, Action.LOCK_USER), "USER cannot lock user");
        assertTrue(accessControl.canPerformAction(currentUser, Action.VIEW_PROFILE), "USER should view profile");
    }
}
