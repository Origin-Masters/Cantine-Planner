package de.htwsaar.cantineplanner.businessLogic.controller;

import de.htwsaar.cantineplanner.businessLogic.CantineService;
import de.htwsaar.cantineplanner.businessLogic.EventManager;
import de.htwsaar.cantineplanner.codegen.tables.records.ReviewRecord;
import de.htwsaar.cantineplanner.codegen.tables.records.UsersRecord;
import de.htwsaar.cantineplanner.exceptions.InvalidEmailTypeException;
import de.htwsaar.cantineplanner.exceptions.UserDoesntExistException;
import de.htwsaar.cantineplanner.exceptions.UserNotValidatedException;
import de.htwsaar.cantineplanner.exceptions.UseriDDoesntExcistException;
import de.htwsaar.cantineplanner.presentation.ScreenManager;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class UserController extends AbstractController {
    public UserController(ScreenManager screenManager,
                          CantineService cantineService,
                          EventManager eventManager,
                          int currentUserId) {
        super(screenManager, cantineService, eventManager, currentUserId);
    }

    @Override
    protected void subscribeToEvents() {

        // User-bezogene Events
        eventManager.subscribe("showEditUserData", (data) -> screenManager.showEditUserDataScreen());
        eventManager.subscribe("editUserData", this::handleEditUserData);
        eventManager.subscribe("showEditNewUserData", (data) -> screenManager.showEditNewUserDataScreen());
        eventManager.subscribe("editNewUserData", this::handleInputNewUserData);
        eventManager.subscribe("showReviewsByUser", this::handleShowReviewsByUser);
        eventManager.subscribe("showAdminMenu", this::handleShowAdminMenu);
        eventManager.subscribe("showAllergenSettings", (data) -> screenManager.showAllergeneSettings());
        eventManager.subscribe("allergeneSettings", this::handleAllergeneSettings);
        eventManager.subscribe("showAllUsers", this::handleAllUser);
        eventManager.subscribe("showDeleteUser", (data) -> screenManager.showDeleteUserScreen());
        eventManager.subscribe("deleteUser", this::handleDeleteUser);
        eventManager.subscribe("showUpdateUserRole", (data) -> screenManager.showUpdateUserRoleScreen());
        eventManager.subscribe("updateUserRole", this::handleUpdateUserRole);

    }

    /**
     * Handles user data editing after verifying current password.
     * <p>
     * Validates the current password and then displays the screen to update user data.
     * </p>
     *
     * @param data an Object array containing the current password as a String
     */
    private void handleEditUserData(Object data) {
        if (data == null) {
            screenManager.showErrorScreen("Data is null");
            return;
        }
        String[] userData = (String[]) data;
        String currentPassword = userData[0];

        try {
            if (cantineService.validateUser(currentUserId, currentPassword)) {
                screenManager.closeActiveWindow();
                screenManager.showSuccessScreen("User validated!");
                screenManager.showEditNewUserDataScreen();
            }
        } catch (UserNotValidatedException e) {
            screenManager.showErrorScreen(e.getMessage());
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while validating user data, please try again!");
        }
    }

    /**
     * Handles updating the user data (password and email).
     * <p>
     * Validates input and updates the current user's data.
     * </p>
     *
     * @param data an Object array containing new password and new email as Strings
     */
    private void handleInputNewUserData(Object data) {
        if (data == null) {
            screenManager.showErrorScreen("Data is null");
            return;
        }
        String[] userData = (String[]) data;
        String newPassword = userData[0];
        String newEmail = userData[1];

        if (isAnyFieldEmpty(newPassword)) {
            screenManager.showErrorScreen("New password is required!");
            return;
        }

        try {
            cantineService.editUserData(currentUserId, newPassword, newEmail);
            screenManager.closeActiveWindow();
            screenManager.showSuccessScreen("Password and email updated successfully!");
        } catch (InvalidEmailTypeException e) {
            screenManager.showErrorScreen(e.getMessage());
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while updating password and email, please try again!");
        }
    }


    /**
     * Handles updating a user's role.
     * <p>
     * Parses user ID and role from input data and updates the user's role.
     * </p>
     *
     * @param data an Object array containing user ID and role as Strings
     */
    private void handleUpdateUserRole(Object data) {
        try {
            String[] dataArray = (String[]) data;
            int userId = Integer.parseInt(dataArray[0]);
            int role = Integer.parseInt(dataArray[1]);
            cantineService.updateUserRole(userId, role);
            screenManager.closeActiveWindow();
            screenManager.showSuccessScreen("User role updated successfully!");
        } catch (NumberFormatException e) {
            screenManager.showErrorScreen("Invalid user ID or role format!");
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while updating user role please try again!");
        }
    }

    /**
     * Handles displaying all reviews made by the current user.
     *
     * @param data not used
     */
    private void handleShowReviewsByUser(Object data) {
        try {
            List<ReviewRecord> reviews = cantineService.getAllReviewsByUser(currentUserId);
            screenManager.showAllReviews(reviews);
        } catch (UseriDDoesntExcistException e) {
            screenManager.showErrorScreen(e.getMessage());
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while fetching all reviews please try again!");
        }
    }

    /**
     * Handles updating allergen settings for the current user.
     * <p>
     * Parses the allergen settings from input data and updates them.
     * </p>
     *
     * @param data an Object array containing allergen settings as Strings
     */
    private void handleAllergeneSettings(Object data){
        try {
            cantineService.setAllergeneSettings(currentUserId, Arrays.toString((String[]) data));
            screenManager.closeActiveWindow();
            screenManager.showSuccessScreen("Allergene settings updated successfully!");
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while setting the allergene settings, please try again!");
        }
    }

    /**
     * Handles showing the admin menu.
     * <p>
     * Verifies if the current user has admin privileges and switches to the admin menu if so.
     * </p>
     *
     * @param data not used
     */
    private void handleShowAdminMenu(Object data) {
        try {
            if (cantineService.isAdmin(currentUserId)) {
                eventManager.notify("switchMenu", 6);
            } else {
                screenManager.showErrorScreen("You are not authorized to access this menu!");
            }
        } catch (SQLException | UserDoesntExistException e) {
            screenManager.showErrorScreen("There was an error while validating the user try again!");
        }
    }

    /**
     * Checks if any provided field is empty.
     *
     * @param fields variable number of String fields
     * @return true if any field is empty, false otherwise
     */
    private boolean isAnyFieldEmpty(String... fields) {
        for (String field : fields) {
            if (Objects.equals(field, "")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Handles displaying all users.
     * <p>
     * Retrieves a list of all users and displays them.
     * </p>
     *
     * @param data not used
     */
    private void handleAllUser(Object data) {
        try{
            List<UsersRecord> users = cantineService.getAllUser();
            screenManager.showAllUser(users);
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while fetching all users please try again!");
        }
    }


    /**
     * Handles deleting a user.
     * <p>
     * Parses the user ID from input data and deletes the corresponding user.
     * </p>
     *
     * @param data an Object array where the first element is the user ID as a String
     */
    private void handleDeleteUser(Object data) {
        try {
            String[] dataArray = (String[]) data;
            int userId = Integer.parseInt(dataArray[0]);
            cantineService.deleteUser(userId);
            screenManager.closeActiveWindow();
            screenManager.showSuccessScreen("User deleted successfully!");
        } catch (NumberFormatException e) {
            screenManager.showErrorScreen("Invalid user ID format!");
        } catch (UseriDDoesntExcistException e) {
            screenManager.showErrorScreen(e.getMessage());
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while deleting user please try again!");
        }
    }








}
