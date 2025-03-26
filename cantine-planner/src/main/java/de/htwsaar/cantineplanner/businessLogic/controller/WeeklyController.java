package de.htwsaar.cantineplanner.businessLogic.controller;

import de.htwsaar.cantineplanner.data.CantineService;
import de.htwsaar.cantineplanner.businessLogic.EventManager;
import de.htwsaar.cantineplanner.businessLogic.controller.eventdata.EventData;
import de.htwsaar.cantineplanner.businessLogic.controller.eventdata.EventType;
import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;
import de.htwsaar.cantineplanner.exceptions.MealDoesntExistException;
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
        eventManager.subscribe(EventType.EDIT_WEEKLY_PLAN_MONDAY, (data) -> screenManager.showEditWeeklyPlanMonday());
        eventManager.subscribe(EventType.EDIT_WEEKLY_PLAN_MONDAY_SUBMIT, this::handleEditWeeklyPlanMonday);
        eventManager.subscribe(EventType.EDIT_WEEKLY_PLAN_TUESDAY, (data) -> screenManager.showEditWeeklyPlanTuesday());
        eventManager.subscribe(EventType.EDIT_WEEKLY_PLAN_TUESDAY_SUBMIT, this::handleEditWeeklyPlanTuesday);
        eventManager.subscribe(EventType.EDIT_WEEKLY_PLAN_WEDNESDAY, (data) -> screenManager.showEditWeeklyPlanWednesday());
        eventManager.subscribe(EventType.EDIT_WEEKLY_PLAN_WEDNESDAY_SUBMIT, this::handleEditWeeklyPlanWednesday);
        eventManager.subscribe(EventType.EDIT_WEEKLY_PLAN_THURSDAY, (data) -> screenManager.showEditWeeklyPlanThursday());
        eventManager.subscribe(EventType.EDIT_WEEKLY_PLAN_THURSDAY_SUBMIT, this::handleEditWeeklyPlanThursday);
        eventManager.subscribe(EventType.EDIT_WEEKLY_PLAN_FRIDAY, (data) -> screenManager.showEditWeeklyPlanFriday());
        eventManager.subscribe(EventType.EDIT_WEEKLY_PLAN_FRIDAY_SUBMIT, this::handleEditWeeklyPlanFriday);
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
                case "Mon" -> 1;
                case "Tue" -> 2;
                case "Wed" -> 3;
                case "Thu" -> 4;
                case "Fri" -> 5;
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
     * Handles editing the meal for Monday in the weekly plan.
     * <p>
     * Updates the meal for Monday based on the provided meal name.
     * </p>
     *
     * @param data an Object array where the first element is the meal name as a String
     */
    public void handleEditWeeklyPlanMonday(EventData data) {
        String[] mealData = (String[]) data.getData();
        String mealName = mealData[0];
        try {
            cantineService.editWeeklyPlan(mealName, "Mon");
            screenManager.closeActiveWindow();
            screenManager.showSuccessScreen("Meal updated for Monday!");
        } catch (SQLException | MealDoesntExistException e) {
            screenManager.showErrorScreen("Error updating meal for Monday: " + e.getMessage());
        }
    }

    /**
     * Handles editing the meal for Tuesday in the weekly plan.
     * <p>
     * Updates the meal for Tuesday based on the provided meal name.
     * </p>
     *
     * @param data an Object array where the first element is the meal name as a String
     */
    public void handleEditWeeklyPlanTuesday(EventData data) {
        String[] mealData = (String[]) data.getData();
        String mealName = mealData[0];
        try {
            cantineService.editWeeklyPlan(mealName, "Tue");
            screenManager.closeActiveWindow();
            screenManager.showSuccessScreen("Meal updated for Tuesday!");
        } catch (SQLException | MealDoesntExistException e) {
            screenManager.showErrorScreen("Error updating meal for Tuesday: " + e.getMessage());
        }
    }

    /**
     * Handles editing the meal for Wednesday in the weekly plan.
     * <p>
     * Updates the meal for Wednesday based on the provided meal name.
     * </p>
     *
     * @param data an Object array where the first element is the meal name as a String
     */
    public void handleEditWeeklyPlanWednesday(EventData data) {
        String[] mealData = (String[]) data.getData();
        String mealName = mealData[0];
        try {
            cantineService.editWeeklyPlan(mealName, "Wed");
            screenManager.closeActiveWindow();
            screenManager.showSuccessScreen("Meal updated for Wednesday!");
        } catch (SQLException | MealDoesntExistException e) {
            screenManager.showErrorScreen("Error updating meal for Wednesday: " + e.getMessage());
        }
    }

    /**
     * Handles editing the meal for Thursday in the weekly plan.
     * <p>
     * Updates the meal for Thursday based on the provided meal name.
     * </p>
     *
     * @param data an Object array where the first element is the meal name as a String
     */
    public void handleEditWeeklyPlanThursday(EventData data) {
        String[] mealData = (String[]) data.getData();
        String mealName = mealData[0];
        try {
            cantineService.editWeeklyPlan(mealName, "Thu");
            screenManager.closeActiveWindow();
            screenManager.showSuccessScreen("Meal updated for Thursday!");
        } catch (SQLException | MealDoesntExistException e) {
            screenManager.showErrorScreen("Error updating meal for Thursday: " + e.getMessage());
        }
    }

    /**
     * Handles editing the meal for Friday in the weekly plan.
     * <p>
     * Updates the meal for Friday based on the provided meal name.
     * </p>
     *
     * @param data an Object array where the first element is the meal name as a String
     */
    public void handleEditWeeklyPlanFriday(EventData data) {
        String[] mealData = (String[]) data.getData();
        String mealName = mealData[0];
        try {
            cantineService.editWeeklyPlan(mealName, "Fri");
            screenManager.closeActiveWindow();
            screenManager.showSuccessScreen("Meal updated for Friday!");
        } catch (SQLException | MealDoesntExistException e) {
            screenManager.showErrorScreen("Error updating meal for Friday: " + e.getMessage());
        }
    }
}
