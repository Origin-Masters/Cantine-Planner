package de.htwsaar.cantineplanner.data;

import de.htwsaar.cantineplanner.codegen.tables.records.UsersRecord;
import de.htwsaar.cantineplanner.dataAccess.HikariCPDataSource;
import de.htwsaar.cantineplanner.exceptions.UserAlreadyExistsException;
import de.htwsaar.cantineplanner.exceptions.UserDoesntExistException;
import de.htwsaar.cantineplanner.exceptions.UserNotValidatedException;
import de.htwsaar.cantineplanner.exceptions.UseriDDoesntExcistException;
import de.htwsaar.cantineplanner.security.PasswordUtil;
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
    void getUser() {

    }

    @Test
    void getAllUser() {
    }

    @Test
    void getUserById() {
    }

    @Test
    void updateUserRole() {
    }

    @Test
    void setAllergeneSettings() {
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
            assertFalse(isAdmin);
        });
    }

    @Test
    public void testValidateUser() {
        assertThrows(UserNotValidatedException.class,
                () -> userRepository.validateUser("invalidUser", "invalidPassword"));
        assertThrows(UserNotValidatedException.class,
                () -> userRepository.validateUser("invalidUser2", "invalidPassword2"));
    }

    @Test
    public void testGetUserId() {
        assertThrows(UserDoesntExistException.class, () -> userRepository.getUserId("nonExistentUser"));
        assertThrows(UserDoesntExistException.class, () -> userRepository.getUserId("nonExistentUser2"));

        //testing for existing user
        assertDoesNotThrow(() -> {
            int userId = userRepository.getUserId("Xudong");
            assertNotNull(userId);
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


}