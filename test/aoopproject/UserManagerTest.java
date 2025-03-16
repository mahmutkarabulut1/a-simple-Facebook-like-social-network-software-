package aoopproject;

import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserManagerTest {

    private UserManager userManager;
    private User quaresma;
    private User icardi;

    @Before
    public void setUp() {
        userManager = UserManager.getInstance();
        userManager.saveAllUsers(); // Ensure the data is cleared before starting the tests
        quaresma = new User("quaresma", "q123", "Ricardo Quaresma", true);
        icardi = new User("icardi", "i123", "Mauro Icardi", true);
    }

    @Test
    public void testAddUser() {
        userManager.addUser(quaresma);
        assertEquals(quaresma, userManager.getUser("quaresma"));
    }

    @Test
    public void testValidateUser() {
        userManager.addUser(quaresma);
        assertTrue(userManager.validateUser("quaresma", "q123"));
        assertFalse(userManager.validateUser("quaresma", "wrongpassword"));
        assertFalse(userManager.validateUser("nonexistent", "password"));
    }

    @Test
    public void testGetLoggedInUser() {
        userManager.addUser(quaresma);
        userManager.validateUser("quaresma", "q123");
        assertEquals(quaresma, userManager.getLoggedInUser());
    }

    @Test
    public void testGetSearchableUsers() {
        userManager.addUser(quaresma);
        userManager.addUser(icardi);
        List<User> searchableUsers = userManager.getSearchableUsers();
        assertTrue(searchableUsers.contains(quaresma));
        assertTrue(searchableUsers.contains(icardi));
    }

    @Test
    public void testSaveAndLoadUser() {
        userManager.addUser(quaresma);
        userManager.saveUser(quaresma);
        User loadedUser = userManager.loadUserByUsername("quaresma");
        assertEquals(quaresma.getUsername(), loadedUser.getUsername());
        assertEquals(quaresma.getPassword(), loadedUser.getPassword());
        assertEquals(quaresma.getFullName(), loadedUser.getFullName());
        assertEquals(quaresma.isSearchable(), loadedUser.isSearchable());
    }

    @Test
    public void testSaveAllUsers() {
        userManager.addUser(quaresma);
        userManager.addUser(icardi);
        userManager.saveAllUsers();
        User loadedUser1 = userManager.loadUserByUsername("quaresma");
        User loadedUser2 = userManager.loadUserByUsername("icardi");
        assertEquals(quaresma.getUsername(), loadedUser1.getUsername());
        assertEquals(icardi.getUsername(), loadedUser2.getUsername());
    }
}
