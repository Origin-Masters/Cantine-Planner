package de.htwsaar.cantineplanner.businessLogic.controller;

import de.htwsaar.cantineplanner.data.CantineService;
import de.htwsaar.cantineplanner.businessLogic.EventManager;
import de.htwsaar.cantineplanner.businessLogic.controller.eventdata.EventData;
import de.htwsaar.cantineplanner.businessLogic.controller.eventdata.EventType;
import de.htwsaar.cantineplanner.businessLogic.controller.eventdata.IntData;
import de.htwsaar.cantineplanner.businessLogic.controller.eventdata.StringArrayData;
import de.htwsaar.cantineplanner.exceptions.InvalidEmailTypeException;
import de.htwsaar.cantineplanner.exceptions.UserAlreadyExistsException;
import de.htwsaar.cantineplanner.exceptions.UserDoesntExistException;
import de.htwsaar.cantineplanner.exceptions.UserNotValidatedException;
import de.htwsaar.cantineplanner.presentation.ScreenManager;

import java.sql.SQLException;
import java.util.Objects;

/**
 * The LoginController class is responsible for handling user authentication and registration.
 * <p>
 * The LoginController class is responsible for handling user authentication and registration, including logging in a user,
 * registering a new user, and displaying the registration screen.
 * </p>
 */
public class LoginController extends AbstractController {

    /**
     * Constructs a new LoginController.
     * <p>
     * This constructor initializes the LoginController with the provided ScreenManager, CantineService, and EventManager.
     * It also subscribes to the relevant events.
     * </p>
     *
     * @param screenManager  the screen manager to manage UI screens
     * @param cantineService the service to handle cantine-related operations
     * @param eventManager   the event manager to handle events
     * @param sessionManager the session manager to manage user sessions
     */
    protected LoginController(ScreenManager screenManager,
                              CantineService cantineService,
                              EventManager eventManager,
                              SessionManager sessionManager) {
        super(screenManager, cantineService, eventManager,sessionManager);
        this.subscribeToEvents();
    }

    /**
     * Subscribes to various user authentication and registration event types and associates them with their respective handlers.
     * <p>
     * This method sets up the event subscriptions for handling user login, registration, and displaying the registration screen.
     * </p>
     */
    @Override
    protected void subscribeToEvents() {
        // User authentication and registration
        eventManager.subscribe(EventType.LOGIN, (data) -> handleLogin((StringArrayData) data));
        eventManager.subscribe(EventType.REGISTER, this::handleRegister);
        eventManager.subscribe(EventType.SHOW_REGISTER_SCREEN, (data) -> screenManager.showInputScreenReg("Register", EventType.REGISTER));
    }

    /**
     * Handles the login event.
     * <p>
     * Validates the user credentials and logs the user in if valid.
     * </p>
     *
     * @param data an Object array containing username and password as Strings
     */
    public void handleLogin(StringArrayData data) {
        if (data == null) {
            screenManager.showErrorScreen("Please fill in all fields!");
            return;
        }
        String[] credentials = (String[]) data.getData();
        String username = credentials[0];
        String password = credentials[1];
        try {
            if (cantineService.validateUser(username, password)) {
                sessionManager.setCurrentUser(cantineService.getUser(username));
                screenManager.closeActiveWindow();
                screenManager.showSuccessScreen("Login successful!");
                eventManager.notify(EventType.SWITCH_MENU, new IntData(1));
            } else {
                screenManager.showErrorScreen("Invalid username or password!");
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
    public void handleRegister(EventData data) {
        try {
            String[] credentials = (String[]) data.getData();
            String username = credentials[0];
            String password = credentials[1];
            String email = credentials[2];
            if (isAnyFieldEmpty(username, password, email)) {
                screenManager.showErrorScreen("Please fill in all fields!");
                return;
            }
            if (cantineService.registerUser(username, password, email)) {
                screenManager.closeActiveWindow();
                screenManager.showSuccessScreen("Registration successful!");
                eventManager.notify(EventType.SWITCH_MENU, new IntData(0));
            }

        } catch (InvalidEmailTypeException | UserAlreadyExistsException e) {
            screenManager.showErrorScreen(e.getMessage());
        } catch (Exception e) {
            screenManager.showErrorScreen("There was an error while registering please try again and check if you filled everything correctly!");
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
}
