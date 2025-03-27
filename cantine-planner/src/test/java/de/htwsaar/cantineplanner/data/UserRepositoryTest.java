package de.htwsaar.cantineplanner.data;

import de.htwsaar.cantineplanner.codegen.tables.records.UsersRecord;
import de.htwsaar.cantineplanner.data.dataAccess.HikariCPDataSource;
import de.htwsaar.cantineplanner.data.exceptions.UserAlreadyExistsException;
import de.htwsaar.cantineplanner.data.exceptions.UserDoesntExistException;
import de.htwsaar.cantineplanner.data.repository.UserRepository;
import de.htwsaar.cantineplanner.data.security.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    UserRepository userRepository;

    @BeforeEach
    public void setUp() {

        String PATH_TO_TEST_PROPERTIES = "hikari-test.properties";
        HikariCPDataSource dataSource = new HikariCPDataSource(PATH_TO_TEST_PROPERTIES);
        userRepository = new UserRepository(dataSource);

    }

    @Test
    public void testValidateUser() {

       assertFalse(userRepository.validateUser("invalidUser", "invalidPassword"));
       assertFalse(userRepository.validateUser("invalidUser2", "invalidPassword2"));

    }

    @Test
    void validateUser() {
        // Assuming 100 and 1000 is a non-existent user ID
        assertFalse( userRepository.validateUser(100, "invalidPassword") );
        assertFalse( userRepository.validateUser(1000, "invalidPassword2") );
    }

    @Test
    void getUserId() {
        assertThrows(UserDoesntExistException.class, () -> userRepository.getUserId("invalidUser"));
        assertDoesNotThrow(() -> {
            int userId = userRepository.getUserId("Xudong");
            assertEquals(16, userId);
        });
    }

    @Test
    void getUser() {
        assertDoesNotThrow(() -> {
            UsersRecord user = userRepository.getUser("Xudong");
            assertNotNull(user);
            assertEquals("Xudong", user.getUsername());
        });
    }

    @Test
    void getAllUser() {
        assertDoesNotThrow(() -> {
            userRepository.getAllUser();
        });
    }

    @Test
    void getUserById() {
        assertDoesNotThrow(() -> {
            UsersRecord user = userRepository.getUserById(16);
            assertNotNull(user);
            assertEquals("Xudong", user.getUsername());
        });
    }

    @Test
    void updateUserRole() {
        assertDoesNotThrow(() -> {
            int originalRole = userRepository.getUserById(16).getRole();
            userRepository.updateUserRole(16, 1);
            UsersRecord user = userRepository.getUserById(16);
            assertEquals(1, user.getRole());
            userRepository.updateUserRole(16, originalRole); // Restore original role
        });
    }


    @Test
    public void testIsAdmin() {
        assertThrows(UserDoesntExistException.class,
                () -> userRepository.isAdmin(9999)); // Assuming 9999 is a non-existent user ID

        assertDoesNotThrow(() -> {
            boolean isAdmin = userRepository.isAdmin(9);
            assertFalse(isAdmin);
        });

        assertDoesNotThrow(() -> {
            boolean isAdmin = userRepository.isAdmin(16);
            assertTrue(isAdmin);
        });
    }


    @Test
    public void testGetUserId() {
        assertThrows(UserDoesntExistException.class, () -> userRepository.getUserId("nonExistentUser"));
        assertThrows(UserDoesntExistException.class, () -> userRepository.getUserId("nonExistentUser2"));

        //testing for existing user
        assertDoesNotThrow(() -> {
            int userId = userRepository.getUserId("Xudong");
        });

    }

    @Test
    public void testRegisterUserWithSameUsername() {
        assertDoesNotThrow(() -> {
            userRepository.deleteUserByName("testUser"); // Delete the user if it already exists
            userRepository.registerUser("testUser", "testPassword", "test@example.com");
        });
        assertThrows(UserAlreadyExistsException.class, () -> {
            userRepository.registerUser("testUser", "testPassword", "test@example.com");
        });
    }

    @Test
    void testIsValidEmail() {
        assertTrue(userRepository.isValidEmail("valid.email@example.com"));
        assertFalse(userRepository.isValidEmail("invalid-email"));
        assertFalse(userRepository.isValidEmail("invalid@.com"));
        assertFalse(userRepository.isValidEmail("invalid@com"));
    }

    @Test
    void testDeleteUserById() {
        // Add a user first
        assertDoesNotThrow(() -> {
            UsersRecord user1 = userRepository.registerUser("testUserToDelete1", "testPassword1",
                    "testToDelete1@example.com");
            assertNotNull(user1);
            int userId1 = user1.getUserid();

            UsersRecord user2 = userRepository.registerUser("testUserToDelete2", "testPassword2",
                    "testToDelete2@example.com");
            assertNotNull(user2);
            int userId2 = user2.getUserid();

            // Now delete the user
            assertDoesNotThrow(() -> userRepository.deleteUserById(userId1));
            assertDoesNotThrow(() -> userRepository.deleteUserById(userId2));
        });
    }

    @Test
    void testDeleteUserByName() {
        // Add a user first
        assertDoesNotThrow(() -> {
            UsersRecord user1 = userRepository.registerUser("testUserToDeleteByName1", "testPassword1",
                    "testToDeleteByName1@example.com");
            assertNotNull(user1);   // Check if user is added successfully

            UsersRecord user2 = userRepository.registerUser("testUserToDeleteByName2", "testPassword2",
                    "testToDeleteByName2@example.com");
            assertNotNull(user2);   // Check if user is added successfully

            // Now delete the user
            assertDoesNotThrow(() -> userRepository.deleteUserByName("testUserToDeleteByName1"));
            assertDoesNotThrow(() -> userRepository.deleteUserByName("testUserToDeleteByName2"));
        });
    }

    @Test
    void testEditUserData() {
        int userId = 8;
        String newPassword = "newPassword";
        String newEmail = "newEmail@example.com";

        assertDoesNotThrow(() -> {
            userRepository.editUserData(userId, newPassword, newEmail);
            UsersRecord user = userRepository.getUserById(userId);
            assertTrue(PasswordUtil.verifyPassword(newPassword, user.getPassword()), "Password does not match!");
            assertEquals(newEmail, user.getEmail());
        });
    }

    @Test
    void setAllergeneSettings() {
        assertDoesNotThrow(() -> {
            int userId = 16;
            String originalAllergeneSettings = userRepository.getUserById(userId).getDontShowMeal();
            userRepository.setAllergeneSettings(userId, "G,T");
            UsersRecord user = userRepository.getUserById(userId);
            assertEquals("G,T", user.getDontShowMeal());
            userRepository.setAllergeneSettings(userId, originalAllergeneSettings); // Restore original settings
        });
    }

}