package de.htwsaar.cantineplanner.businessLogic.controller;

import de.htwsaar.cantineplanner.data.CantineService;
import de.htwsaar.cantineplanner.businessLogic.EventManager;
import de.htwsaar.cantineplanner.businessLogic.controller.eventdata.EventData;
import de.htwsaar.cantineplanner.businessLogic.controller.eventdata.EventType;
import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;
import de.htwsaar.cantineplanner.presentation.ScreenManager;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

/**
 * The WeeklyController class is responsible for handling the weekly meal plan.
 * <p>
 * The WeeklyController class is responsible for handling the weekly meal plan, including displaying the weekly menu,
 * showing the weekly plan, editing the weekly plan, and resetting the weekly plan.
 * </p>
 */
public class WeeklyController extends AbstractController {
    private String currentWeekdayEdit;
    /**
     * Constructs a new WeeklyController.
     * <p>
     * This constructor initializes the WeeklyController with the provided ScreenManager, CantineService, and EventManager.
     * It also subscribes to the relevant events.
     * </p>
     *
     * @param screenManager  the screen manager to manage UI screens
     * @param cantineService the service to handle cantine-related operations
     * @param eventManager   the event manager to handle events
     * @param sessionManager the session manager to manage user sessions
     */
    protected WeeklyController(ScreenManager screenManager,
                            CantineService cantineService,
                            EventManager eventManager,
                            SessionManager sessionManager) {
        super(screenManager, cantineService, eventManager, sessionManager);
        this.subscribeToEvents();
        currentWeekdayEdit = null;
    }

    /**
     * Subscribes to various event types and associates them with their respective handlers.
     * <p>
     * This method sets up the event subscriptions for handling different weekly plan events such as showing,
     * editing, and resetting the weekly plan for each day of the week.
     * </p>
     */
    @Override
    protected void subscribeToEvents() {
        eventManager.subscribe(EventType.SHOW_WEEKLY_PLAN, this::handleShowWeeklyPlan);
        eventManager.subscribe(EventType.EDIT_WEEKLY_PLAN, this::handleShowEditWeeklyPlan);
        eventManager.subscribe(EventType.RESET_WEEKLY_PLAN, this::handleResetWeeklyPlan);

        eventManager.subscribe(EventType.EDIT_WEEKLY_PLAN_WEEKDAY, this::handleShowEditWeekdayPlan);
        eventManager.subscribe(EventType.EDIT_WEEKLY_PLAN_WEEKDAY_SUBMIT, this::handleEditWeeklyPlan);
    }

    /**
     * Displays the weekly menu screen.
     * <p>
     * This method checks if the current user is an admin and displays the weekly menu screen accordingly.
     * If an error occurs while validating the user, an error screen is shown.
     * </p>
     */
    protected void showWeeklyMenu() {
        try {
            screenManager.showWeeklyMenuScreen(cantineService.isAdmin(sessionManager.getCurrentUserId()));
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while validating the user. Try again!");
        }
    }

    /**
     * Handles displaying the weekly meal plan.
     * <p>
     * Retrieves the weekly plan, sorts it by day, and displays it.
     * </p>
     */
    private void handleShowWeeklyPlan() {
        try {
            List<MealsRecord> weeklyPlan = cantineService.getWeeklyPlan();
            // Sort the weeklyPlan by the day field
            weeklyPlan.sort(Comparator.comparing(meal -> switch (meal.getDay()) {
                case "Monday" -> 1;
                case "Tuesday" -> 2;
                case "Wednesday" -> 3;
                case "Thursday" -> 4;
                case "Friday" -> 5;
                default -> 6;
            }));
            screenManager.showWeeklyPlanScreen(weeklyPlan);
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while fetching the weekly plan, please try again!");
        }
    }


    /**
     * Handles displaying the edit weekly plan screen.
     */
    private void handleShowEditWeeklyPlan() {
        screenManager.showEditWeeklyPlanScreen();
    }

    private void handleShowEditWeekdayPlan(EventData data) {
        try {
            int weekdayIndex = Integer.parseInt(data.getData().toString());
            currentWeekdayEdit = Weekday.getDisplayNameBySortOrder(weekdayIndex);
            screenManager.showEditWeeklyPlanWeekday(currentWeekdayEdit);
        } catch (Exception e) {
            screenManager.showErrorScreen("There was an error while editing the meal-Plan");
        }
    }

    /**
     * Handles resetting the weekly meal plan to its default state.
     */
    private void handleResetWeeklyPlan() {
        try {
            cantineService.resetWeeklyPlan();
            screenManager.showSuccessScreen("Weekly plan has been reset!");
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while resetting the weekly plan, please try again!");
        }
    }

    /**
     * Handles editing the meal for a specific day in the weekly plan.
     * <p>
     * This generic method processes meal updates for any weekday, extracting the meal name
     * from the provided data and updating the weekly plan through the cantine service.
     * It displays a success message upon successful update or an appropriate error message
     * if the update fails.
     * </p>
     *
     * @param data the EventData containing the meal information, where the first element
     *             of the data array is expected to be the meal name
     * @throws ClassCastException if the data cannot be cast to a String array
     * @throws ArrayIndexOutOfBoundsException if the data array is empty
     */
    private void handleEditWeeklyPlan(EventData data) {
        try {
            String[] mealData = (String[]) data.getData();
            String mealName = mealData[0];

            cantineService.editWeeklyPlan(mealName, currentWeekdayEdit);
            screenManager.closeActiveWindow();
            screenManager.showSuccessScreen("Meal updated for " + currentWeekdayEdit + "!");
        } catch (Exception e) {
            screenManager.showErrorScreen("Error updating meal plan: " + e.getMessage());
        }
    }

}
