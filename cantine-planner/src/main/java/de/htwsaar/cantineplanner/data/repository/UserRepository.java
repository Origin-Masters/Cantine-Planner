package de.htwsaar.cantineplanner.data.repository;

import de.htwsaar.cantineplanner.codegen.tables.Users;
import de.htwsaar.cantineplanner.codegen.tables.records.UsersRecord;
import de.htwsaar.cantineplanner.data.dataAccess.HikariCPDataSource;
import de.htwsaar.cantineplanner.data.exceptions.InvalidEmailTypeException;
import de.htwsaar.cantineplanner.data.exceptions.UserAlreadyExistsException;
import de.htwsaar.cantineplanner.data.exceptions.UserDoesntExistException;
import de.htwsaar.cantineplanner.data.exceptions.UserNotValidatedException;
import de.htwsaar.cantineplanner.data.security.PasswordUtil;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * The UserRepository class is responsible for handling user data in the database.
 */
public class UserRepository extends AbstractRepository {
    /**
     * Constructs a new UserRepository object.
     * @param dataSource an instance of HikariCPDataSource, offering a connection pool
     * for efficient and reliable database connectivity.
     */
    public UserRepository(HikariCPDataSource dataSource) {
        super(dataSource);
    }

    /**
     * Validates a user by username and password.
     * <p>
     * This method checks if the provided username and plain text password match the stored hashed password in the database.
     * If the username or password is invalid, a UserNotValidatedException is thrown.
     * </p>
     *
     * @param username          the username of the user to be validated
     * @param plainTextPassword the plain text password of the user to be validated
     * @return true if the user is successfully validated
     * @throws UserNotValidatedException if the username or password is invalid
     */
    public boolean validateUser(String username, String plainTextPassword) throws UserNotValidatedException {
        try (Connection connection = dataSource.getConnection()) {
            var dsl = getDSLContext(connection);
            String hashedPassword = dsl.select(Users.USERS.PASSWORD).from(Users.USERS).where(
                    Users.USERS.USERNAME.eq(username)).fetchOne(Users.USERS.PASSWORD);

            if (hashedPassword == null || !PasswordUtil.verifyPassword(plainTextPassword, hashedPassword)) {
                throw new UserNotValidatedException("Invalid username or password!");
            }
            return true;
        }
        catch( UserNotValidatedException | SQLException exception){
            return false;
        }

    }

    /**
     * Validates a user by user ID and password.
     * <p>
     * This method checks if the provided user ID and plain text password match the stored hashed password in the database.
     * If the password is invalid, a UserNotValidatedException is thrown.
     * </p>
     *
     * @param userID            the ID of the user to be validated
     * @param plainTextPassword the plain text password of the user to be validated
     * @return true if the user is successfully validated
     * @throws SQLException              if a database access error occurs
     * @throws UserNotValidatedException if the password is invalid
     */
    public boolean validateUser(int userID, String plainTextPassword) throws UserNotValidatedException {
        try (Connection connection = dataSource.getConnection()) {
            var dsl = getDSLContext(connection);
            String hashedPassword = dsl.select(Users.USERS.PASSWORD).from(Users.USERS).where(
                    Users.USERS.USERID.eq(userID)).fetchOne(Users.USERS.PASSWORD);

            if (hashedPassword == null || !PasswordUtil.verifyPassword(plainTextPassword, hashedPassword)) {
                throw new UserNotValidatedException("Invalid password!");
            }
            return true;
        }

        catch(UserNotValidatedException | SQLException exception){
            return false;
        }
    }

    /**
     * Retrieves the user ID for a given username.
     * <p>
     * This method fetches the user ID from the database based on the provided username.
     * If the username does not exist, a UserDoesntExistException is thrown.
     * </p>
     *
     * @param username the username of the user whose ID is to be retrieved
     * @return the user ID of the user with the given username
     * @throws SQLException             if a database access error occurs
     * @throws UserDoesntExistException if the user with the given username does not exist
     */
    public int getUserId(String username) throws SQLException, UserDoesntExistException {
        try (Connection connection = dataSource.getConnection()) {
            var dsl = getDSLContext(connection);

            if (!dsl.fetchExists(dsl.selectFrom(Users.USERS).where(Users.USERS.USERNAME.eq(username)))) {
                throw new UserDoesntExistException("The user with the given username doesn't exist!");
            }

            return dsl.select(Users.USERS.USERID).from(Users.USERS).where(Users.USERS.USERNAME.eq(username)).fetchOne(
                    Users.USERS.USERID);
        }
    }

    /**
     * Retrieves a user record by username from the database.
     * <p>
     * This method fetches the user record from the database based on the provided username.
     * If the username does not exist, a UserDoesntExistException is thrown.
     * </p>
     *
     * @param username the username of the user to be retrieved
     * @return the UsersRecord of the user with the given username
     * @throws SQLException             if a database access error occurs
     * @throws UserDoesntExistException if the user with the given username does not exist
     */
    public UsersRecord getUser(String username) throws SQLException, UserDoesntExistException {
        try (Connection connection = dataSource.getConnection()) {
            var dsl = getDSLContext(connection);

            if (!dsl.fetchExists(dsl.selectFrom(Users.USERS).where(Users.USERS.USERNAME.eq(username)))) {
                throw new UserDoesntExistException("The user with the given username doesn't exist!");
            }

            return dsl.selectFrom(Users.USERS).where(Users.USERS.USERNAME.eq(username)).fetchOne();
        }
    }

    /**
     * Retrieves all users from the database.
     * <p>
     * This method fetches all user records from the database.
     * </p>
     *
     * @return a list of UsersRecord objects representing all users
     * @throws SQLException if a database access error occurs
     */
    public List<UsersRecord> getAllUser() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            var dsl = getDSLContext(connection);
            return dsl.selectFrom(Users.USERS).fetchInto(UsersRecord.class);
        }
    }

    /**
     * Retrieves a user record by user ID from the database.
     * <p>
     * This method fetches the user record from the database based on the provided user ID.
     * If the user ID does not exist, a UserDoesntExistException is thrown.
     * </p>
     *
     * @param userId the ID of the user to be retrieved
     * @return the UsersRecord of the user with the given ID
     * @throws SQLException             if a database access error occurs
     * @throws UserDoesntExistException if the user with the given ID does not exist
     */
    public UsersRecord getUserById(int userId) throws SQLException, UserDoesntExistException {
        try (Connection connection = dataSource.getConnection()) {
            var dsl = getDSLContext(connection);

            if (!dsl.fetchExists(dsl.selectFrom(Users.USERS).where(Users.USERS.USERID.eq(userId)))) {
                throw new UserDoesntExistException("The user with the given UserId doesn't exist!");
            }

            return dsl.selectFrom(Users.USERS).where(Users.USERS.USERID.eq(userId)).fetchOne();
        }
    }

    /**
     * Updates the role of a user by user ID.
     * <p>
     * This method updates the role of a user in the database based on the provided user ID.
     * If the user ID does not exist, a UserDoesntExistException is thrown.
     * </p>
     *
     * @param userId the ID of the user whose role is to be updated
     * @param role   the new role to be assigned to the user
     * @throws SQLException             if a database access error occurs
     * @throws UserDoesntExistException if the user with the given ID does not exist
     */
    public void updateUserRole(int userId, int role) throws SQLException, UserDoesntExistException {
        try (Connection connection = dataSource.getConnection()) {
            var dsl = getDSLContext(connection);

            if (!dsl.fetchExists(dsl.selectFrom(Users.USERS).where(Users.USERS.USERID.eq(userId)))) {
                throw new UserDoesntExistException("The user with the given UserId doesn't exist!");
            }

            dsl.update(Users.USERS).set(Users.USERS.ROLE, role).where(Users.USERS.USERID.eq(userId)).execute();
        }
    }

    /**
     * Checks if a user is an admin by user ID.
     * <p>
     * This method checks if the user with the given user ID has an admin role.
     * If the user ID does not exist, a UserDoesntExistException is thrown.
     * </p>
     *
     * @param userID the ID of the user to be checked
     * @return true if the user is an admin, false otherwise
     * @throws SQLException             if a database access error occurs
     * @throws UserDoesntExistException if the user with the given ID does not exist
     */
    public boolean isAdmin(int userID) throws SQLException, UserDoesntExistException {
        try (Connection connection = dataSource.getConnection()) {
            var dsl = getDSLContext(connection);

            if (!dsl.fetchExists(dsl.selectFrom(Users.USERS).where(Users.USERS.USERID.eq(userID)))) {
                throw new UserDoesntExistException("The user with the given ID doesn't exist!");
            }
            return dsl.select(Users.USERS.ROLE).from(Users.USERS).where(Users.USERS.USERID.eq(userID)).fetchOne(
                    Users.USERS.ROLE) == 1;
        }
    }

    /**
     * Registers a new user in the database.
     * <p>
     * This method inserts a new user record into the database with the provided username, password, and email.
     * If the username already exists, a UserAlreadyExistsException is thrown.
     * If the email is invalid, an InvalidEmailTypeException is thrown.
     * </p>
     *
     * @param username          the username of the user to be registered
     * @param plainTextPassword the plain text password of the user to be registered
     * @param email             the email of the user to be registered
     * @return the UsersRecord of the registered user
     * @throws SQLException               if a database access error occurs
     * @throws UserAlreadyExistsException if the username already exists
     * @throws InvalidEmailTypeException  if the email is invalid
     */
    public UsersRecord registerUser(String username, String plainTextPassword, String email) throws SQLException, UserAlreadyExistsException, InvalidEmailTypeException {
        try (Connection connection = dataSource.getConnection()) {
            var dsl = getDSLContext(connection);

            if (dsl.fetchExists(dsl.selectFrom(Users.USERS).where(Users.USERS.USERNAME.eq(username)))) {
                throw new UserAlreadyExistsException("Username already exists, please choose another one!");
            }

            if (email != null && !isValidEmail(email)) {
                throw new InvalidEmailTypeException("Invalid email type!");
            }
            String hashedPassword = PasswordUtil.hashPassword(plainTextPassword);
            dsl.insertInto(Users.USERS).set(Users.USERS.USERNAME, username).set(Users.USERS.PASSWORD,
                    hashedPassword).set(Users.USERS.EMAIL, email).set(Users.USERS.ROLE, 0).execute();

            return dsl.selectFrom(Users.USERS).where(Users.USERS.USERNAME.eq(username)).fetchOne();
        }
    }

    /**
     * Validates an email address.
     * <p>
     * This method checks if the provided email address matches the standard email format.
     * </p>
     *
     * @param email the email address to be validated
     * @return true if the email address is valid, false otherwise
     */
    public boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    /**
     * Deletes a user from the database by user ID.
     * <p>
     * This method deletes a user record from the database based on the provided user ID.
     * If the user ID does not exist, a UserDoesntExistException is thrown.
     * </p>
     *
     * @param userId the ID of the user to be deleted
     * @throws SQLException             if a database access error occurs
     * @throws UserDoesntExistException if the user with the given ID does not exist
     */
    public void deleteUserById(int userId) throws SQLException, UserDoesntExistException {
        try (Connection connection = dataSource.getConnection()) {
            var dsl = getDSLContext(connection);

            if (!dsl.fetchExists(dsl.selectFrom(Users.USERS).where(Users.USERS.USERID.eq(userId)))) {
                throw new UserDoesntExistException("The user with the given ID doesn't exist!");
            }

            dsl.deleteFrom(Users.USERS).where(Users.USERS.USERID.eq(userId)).execute();
        }
    }

    /**
     * Deletes a user from the database by username.
     * <p>
     * This method deletes a user record from the database based on the provided username.
     * If the username does not exist, a UserDoesntExistException is thrown.
     * </p>
     *
     * @param UserName the username of the user to be deleted
     * @throws SQLException             if a database access error occurs
     * @throws UserDoesntExistException if the user with the given username does not exist
     */
    public void deleteUserByName(String UserName) throws SQLException, UserDoesntExistException {
        try (Connection connection = dataSource.getConnection()) {
            var dsl = getDSLContext(connection);

            if (!dsl.fetchExists(dsl.selectFrom(Users.USERS).where(Users.USERS.USERNAME.eq(UserName)))) {
                throw new UserDoesntExistException("The user with the given username doesn't exist!");
            }

            dsl.deleteFrom(Users.USERS).where(Users.USERS.USERNAME.eq(UserName)).execute();
        }
    }

    /**
     * Edits the user data in the database.
     * <p>
     * This method updates the user's password and/or email in the database based on the provided user ID.
     * If the new email is invalid, an InvalidEmailTypeException is thrown.
     * </p>
     *
     * @param currentUserId the ID of the user whose data is to be edited
     * @param newPassword   the new password to be set for the user (can be null or empty if not changing)
     * @param newEmail      the new email to be set for the user (can be null or empty if not changing)
     * @throws SQLException              if a database access error occurs
     * @throws InvalidEmailTypeException if the new email is invalid
     */
    public void editUserData(int currentUserId, String newPassword, String newEmail) throws SQLException, InvalidEmailTypeException {
        try (Connection connection = dataSource.getConnection()) {
            var dsl = getDSLContext(connection);

            if (newEmail != null && !isValidEmail(newEmail)) {
                throw new InvalidEmailTypeException("Invalid email type!");
            }

            // Update the user's password if provided
            if (newPassword != null && !newPassword.isEmpty()) {
                String hashedPassword = PasswordUtil.hashPassword(newPassword);
                dsl.update(DSL.table("users")).set(DSL.field("password"), hashedPassword).where(
                        DSL.field("userid").eq(currentUserId)).execute();
            }

            // Update the user's email if provided
            if (newEmail != null && !newEmail.isEmpty()) {
                dsl.update(DSL.table("users")).set(DSL.field("email"), newEmail).where(
                        DSL.field("userid").eq(currentUserId)).execute();
            }
        }
    }

    /**
     * Sets allergies for a user by user ID.
     * <p>
     * This method updates the user's allergy settings in the database based on the provided user ID.
     * </p>
     *
     * @param userId    the ID of the user whose allergy settings are to be updated
     * @param allergies the allergies to be set for the user
     * @throws SQLException if a database access error occurs
     */
    public void setAllergeneSettings(int userId, String allergies) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            var dsl = getDSLContext(connection);
            dsl.update(Users.USERS)
                    .set(Users.USERS.DONT_SHOW_MEAL, allergies)
                    .where(Users.USERS.USERID.eq(userId))
                    .execute();
        }
    }
}
