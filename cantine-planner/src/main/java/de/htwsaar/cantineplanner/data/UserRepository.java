package de.htwsaar.cantineplanner.data;

import de.htwsaar.cantineplanner.codegen.tables.Users;
import de.htwsaar.cantineplanner.codegen.tables.records.UsersRecord;
import de.htwsaar.cantineplanner.dataAccess.HikariCPDataSource;
import de.htwsaar.cantineplanner.exceptions.*;
import de.htwsaar.cantineplanner.security.PasswordUtil;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class UserRepository extends AbstractRepository {

    protected UserRepository(HikariCPDataSource dataSource) {
        super(dataSource);
    }


    /**
     * Method validateUser validates a user by username and password
     *
     * @param username          of type String
     * @param plainTextPassword of type String
     * @throws UserNotValidatedException if the user is not validated
     */
    public boolean validateUser(String username, String plainTextPassword) throws SQLException, UserNotValidatedException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            String hashedPassword = dsl.select(Users.USERS.PASSWORD).from(Users.USERS).where(
                    Users.USERS.USERNAME.eq(username)).fetchOne(Users.USERS.PASSWORD);

            if (hashedPassword == null || !PasswordUtil.verifyPassword(plainTextPassword, hashedPassword)) {
                throw new UserNotValidatedException("Invalid username or password!");
            }
            return true;
        }
    }

    /**
     * Method validateUser validates a user by username and password
     *
     * @param userID            of type Int
     * @param plainTextPassword of type String
     * @throws UserNotValidatedException if the user is not validated
     */
    public boolean validateUser(int userID, String plainTextPassword) throws SQLException, UserNotValidatedException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            String hashedPassword = dsl.select(Users.USERS.PASSWORD).from(Users.USERS).where(
                    Users.USERS.USERID.eq(userID)).fetchOne(Users.USERS.PASSWORD);

            if (hashedPassword == null || !PasswordUtil.verifyPassword(plainTextPassword, hashedPassword)) {
                throw new UserNotValidatedException("Invalid password!");
            }
            return true;
        }
    }

    /**
     * Method getUserId searches for a user by username and returns the userId
     *
     * @param username of type String
     * @return userId of type int
     * @throws SQLException             if an SQL exception occurs
     * @throws UserDoesntExistException if the user doesn't exist
     */
    public int getUserId(String username) throws SQLException, UserDoesntExistException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);

            if (!dsl.fetchExists(dsl.selectFrom(Users.USERS).where(Users.USERS.USERNAME.eq(username)))) {
                throw new UserDoesntExistException("The user with the given username doesn't exist!");
            }

            return dsl.select(Users.USERS.USERID).from(Users.USERS).where(Users.USERS.USERNAME.eq(username)).fetchOne(
                    Users.USERS.USERID);
        }
    }

    /**
     * Method getUser retrieves a user by username
     * @param username
     * @return UsersRecord
     * @throws SQLException if an SQL exception occurs
     * @throws UserDoesntExistException if the user doesn't exist
     */
    public UsersRecord getUser(String username) throws SQLException, UserDoesntExistException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);

            if (!dsl.fetchExists(dsl.selectFrom(Users.USERS).where(Users.USERS.USERNAME.eq(username)))) {
                throw new UserDoesntExistException("The user with the given username doesn't exist!");
            }

            return dsl.selectFrom(Users.USERS).where(Users.USERS.USERNAME.eq(username)).fetchOne();
        }
    }
    /**
     * Method getAllUsers retrieves all users from the database
     *
     * @return List of UsersRecord
     */
    public List<UsersRecord> getAllUser() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            return dsl.selectFrom(Users.USERS).fetchInto(UsersRecord.class);
        }
    }

    /**
     * Retrieves a user record by user ID from the database.
     *
     * @param userId the ID of the user to retrieve
     * @return the UsersRecord of the user with the given ID
     * @throws SQLException             if a database access error occurs
     * @throws UserDoesntExistException if the user with the given ID doesn't exist
     */
    public UsersRecord getUserById(int userId) throws SQLException, UserDoesntExistException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);

            if (!dsl.fetchExists(dsl.selectFrom(Users.USERS).where(Users.USERS.USERID.eq(userId)))) {
                throw new UserDoesntExistException("The user with the given UserId doesn't exist!");
            }

            return dsl.selectFrom(Users.USERS).where(Users.USERS.USERID.eq(userId)).fetchOne();
        }
    }

    /**
     * Method updateUserRole updates the role of a user by userId
     *
     * @param userId
     * @param role
     * @throws SQLException
     * @throws UserDoesntExistException
     */
    public void updateUserRole(int userId, int role) throws SQLException, UserDoesntExistException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);

            if (!dsl.fetchExists(dsl.selectFrom(Users.USERS).where(Users.USERS.USERID.eq(userId)))) {
                throw new UserDoesntExistException("The user with the given UserId doesn't exist!");
            }

            dsl.update(Users.USERS).set(Users.USERS.ROLE, role).where(Users.USERS.USERID.eq(userId)).execute();
        }
    }

    /**
     * Method isAdmin checks if a user is an admin or not
     *
     * @return boolean true if the user is an admin, false otherwise
     * @throws SQLException             if an SQL exception occurs
     * @throws UserDoesntExistException if the user doesn't exist
     */
    public boolean isAdmin(int UserID) throws SQLException, UseriDDoesntExcistException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);

            if (!dsl.fetchExists(dsl.selectFrom(Users.USERS).where(Users.USERS.USERID.eq(UserID)))) {
                throw new UseriDDoesntExcistException("The user with the given username doesn't exist!");
            }
            return dsl.select(Users.USERS.ROLE).from(Users.USERS).where(Users.USERS.USERID.eq(UserID)).fetchOne(
                    Users.USERS.ROLE) == 1;
        }
    }

    /**
     * Method registerUser registers a user in the database with the given username, password and email
     *
     * @param username          of type String
     * @param plainTextPassword of type String
     * @param email             of type String
     * @return A UsersRecord of the registered user
     * @throws SQLException               if an SQL exception occurs
     * @throws UserAlreadyExistsException if the user already exists
     */
    public UsersRecord registerUser(String username, String plainTextPassword, String email) throws SQLException, UserAlreadyExistsException, InvalidEmailTypeException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);

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
     * Method isValidEmail checks if the email is valid
     *
     * @param email of type String
     * @return boolean true if the email is valid, false otherwise
     */
    public boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    /**
     * Method deleteUserById deletes a user from the database by userId
     *
     * @param userId of type int of the user to be deleted
     * @throws SQLException             if an SQL exception occurs
     * @throws UserDoesntExistException if the user doesn't exist
     */
    public void deleteUserById(int userId) throws SQLException, UserDoesntExistException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);

            if (!dsl.fetchExists(dsl.selectFrom(Users.USERS).where(Users.USERS.USERID.eq(userId)))) {
                throw new UserDoesntExistException("The user with the given ID doesn't exist!");
            }

            dsl.deleteFrom(Users.USERS).where(Users.USERS.USERID.eq(userId)).execute();
        }
    }

    /**
     * Method deleteUserByName deletes a user from the database by username
     *
     * @param UserName of type String of the user to be deleted
     * @throws SQLException             if an SQL exception occurs
     * @throws UserDoesntExistException if the user doesn't exist
     */
    public void deleteUserByName(String UserName) throws SQLException, UserDoesntExistException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);

            if (!dsl.fetchExists(dsl.selectFrom(Users.USERS).where(Users.USERS.USERNAME.eq(UserName)))) {
                throw new UserDoesntExistException("The user with the given username doesn't exist!");
            }

            dsl.deleteFrom(Users.USERS).where(Users.USERS.USERNAME.eq(UserName)).execute();
        }
    }

    /**
     * Method editUserData edits the user data in the database
     *
     * @param currentUserId of type int
     * @param newPassword   of type String
     * @param newEmail      of type String
     * @throws SQLException             if an SQL exception occurs
     * @throws UserDoesntExistException if the user doesn't exist
     */
    public void editUserData(int currentUserId, String newPassword, String newEmail) throws SQLException, InvalidEmailTypeException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);

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
     * Retrieves the allergy settings for a user by user ID.
     *
     * @param userId the ID of the user
     * @return a list of allergies
     * @throws SQLException             if a database access error occurs
     * @throws UserDoesntExistException if the user with the given ID doesn't exist
     */
    private List<String> getUserAllergies(int userId) throws SQLException, UserDoesntExistException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);

            UsersRecord user = dsl.selectFrom(Users.USERS)
                    .where(Users.USERS.USERID.eq(userId))
                    .fetchOne();

            if (user == null) {
                throw new UserDoesntExistException("The user with the given UserId doesn't exist!");
            }

            String allergies = user.getDontShowMeal();
            return Arrays.asList(allergies.split(","));
        }
    }





}
