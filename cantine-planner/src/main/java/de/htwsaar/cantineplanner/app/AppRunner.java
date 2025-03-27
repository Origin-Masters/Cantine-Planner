package de.htwsaar.cantineplanner.app;

import de.htwsaar.cantineplanner.businessLogic.manager.EventManager;
import de.htwsaar.cantineplanner.businessLogic.manager.SessionManager;
import de.htwsaar.cantineplanner.businessLogic.service.CantineService;
import de.htwsaar.cantineplanner.businessLogic.controller.MainController;
import de.htwsaar.cantineplanner.data.util.DataBaseUtil;
import de.htwsaar.cantineplanner.presentation.ScreenManager;

/**
 * The AppRunner class is responsible for bootstrapping the application.
 *
 * <p>
 * It securely loads the initial database, initializes essential components
 * such as the EventManager, CantineService, ScreenManager, and SessionManager,
 * and creates the MainController that manages the application's lifecycle.
 * </p>
 *
 * <p>
 * This class is intended to abstract the initialization process so that the main
 * method simply calls {@code start()}.
 * </p>
 */
public class AppRunner {

    private final MainController mainController;

    /**
     * Constructs an AppRunner instance.
     *
     * <p>
     * This constructor performs the following actions:
     * <ul>
     *   <li>Loads and validates the initial database from the specified path.</li>
     *   <li>Initializes the EventManager and CantineService using secure configurations.</li>
     *   <li>Instantiates ScreenManager and SessionManager to manage UI screens and user sessions.</li>
     *   <li>Creates the MainController, passing in all necessary dependencies.</li>
     * </ul>
     * </p>
     */
    public AppRunner() {
        DataBaseUtil.loadInitialDataBase("./database/database.db");



        EventManager eventManager = new EventManager();
        CantineService cantineService = new CantineService();
        ScreenManager screenManager = new ScreenManager(eventManager, cantineService);
        SessionManager sessionManager = new SessionManager();

        this.mainController = new MainController(screenManager, cantineService, eventManager, sessionManager);
    }

    /**
     * Starts the main controller, launching the application's execution.
     *
     * <p>
     * This method delegates the startup process to the MainController, which then
     * handles further initialization and event processing.
     * </p>
     */
    public void start() {
        mainController.start();
    }
}