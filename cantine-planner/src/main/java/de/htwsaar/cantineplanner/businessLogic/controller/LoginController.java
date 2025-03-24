package de.htwsaar.cantineplanner.businessLogic.controller;

import de.htwsaar.cantineplanner.businessLogic.CantineService;
import de.htwsaar.cantineplanner.businessLogic.EventManager;
import de.htwsaar.cantineplanner.codegen.tables.records.UsersRecord;
import de.htwsaar.cantineplanner.exceptions.InvalidEmailTypeException;
import de.htwsaar.cantineplanner.exceptions.UserAlreadyExistsException;
import de.htwsaar.cantineplanner.exceptions.UserDoesntExistException;
import de.htwsaar.cantineplanner.exceptions.UserNotValidatedException;
import de.htwsaar.cantineplanner.presentation.ScreenManager;

import java.sql.SQLException;
import java.util.Objects;

public class LoginController extends AbstractController {

    public LoginController(ScreenManager screenManager,
                          CantineService cantineService,
                          EventManager eventManager,
                           UsersRecord userRecord ) {
        super(screenManager, cantineService, eventManager, userRecord);
        this.subscribeToEvents();
    }
    @Override
    protected void subscribeToEvents() {

        // User-Authentifizierung und Registrierung
        eventManager.subscribe("login", this::handleLogin);
        eventManager.subscribe("register", this::handleRegister);
        eventManager.subscribe("showRegisterScreen", this::handleShowRegisterScreen);
    }
    // User-Handler

    /**
     * Handles the login event.
     * <p>
     * Validates the user credentials and logs the user in if valid.
     * </p>
     *
     * @param data an Object array containing username and password as Strings
     */
    public void handleLogin(Object data) {
        String[] credentials = (String[]) data;
        String username = credentials[0];
        String password = credentials[1];
        try {
            if (cantineService.validateUser(username, password)) {
                currentUser = cantineService.getUser(username);
                screenManager.closeActiveWindow();
                screenManager.showSuccessScreen("Login successful!");
                eventManager.notify("switchMenu", 1);
            }
        } catch (UserNotValidatedException e) {
            screenManager.showErrorScreen(e.getMessage());
        } catch (SQLException | UserDoesntExistException e) {
            screenManager.showErrorScreen("There was an error while logging in please try again!");
        }
    }

    /**
     * Handles user registration.
     * <p>
     * Registers a new user with the provided credentials if all fields are filled.
     * </p>
     *
     * @param data an Object array containing username, password, and email as Strings
     */
    public void handleRegister(Object data) {
        if (data == null) {
            screenManager.showErrorScreen("Data is null");
            return;
        }
        String[] credentials = (String[]) data;
        String username = credentials[0];
        String password = credentials[1];
        String email = credentials[2];
        if (isAnyFieldEmpty(username, password, email)) {
            screenManager.showErrorScreen("Please fill in all fields!");
            return;
        }

        try {
            if (cantineService.registerUser(username, password, email)) {
                screenManager.closeActiveWindow();
                screenManager.showSuccessScreen("Registration successful!");
                eventManager.notify("switchMenu", 0);
            }

        } catch (InvalidEmailTypeException | UserAlreadyExistsException e) {
            screenManager.showErrorScreen(e.getMessage());
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while registering please try again!");
        }
    }

    /**
     * Displays the registration input screen.
     *
     * @param data not used
     */
    private void handleShowRegisterScreen(Object data) {
        screenManager.showInputScreenReg("Register", "register");
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
}
