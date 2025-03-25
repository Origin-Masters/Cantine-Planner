package de.htwsaar.cantineplanner.businessLogic.controller;

import de.htwsaar.cantineplanner.businessLogic.CantineService;
import de.htwsaar.cantineplanner.businessLogic.EventManager;
import de.htwsaar.cantineplanner.businessLogic.controller.eventdata.EventData;
import de.htwsaar.cantineplanner.businessLogic.controller.eventdata.EventType;
import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;
import de.htwsaar.cantineplanner.exceptions.MealDoesntExistException;
import de.htwsaar.cantineplanner.presentation.ScreenManager;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

public class WeeklyController extends AbstractController {
    public WeeklyController(ScreenManager screenManager,
                            CantineService cantineService,
                            EventManager eventManager) {
        super(screenManager, cantineService, eventManager);
        this.subscribeToEvents();
    }

    @Override
    protected void subscribeToEvents() {

        eventManager.subscribe(EventType.SHOW_WEEKLY_PLAN, this::handleShowWeeklyPlan);
        eventManager.subscribe(EventType.EDIT_WEEKLY_PLAN, this::handleShowEditWeeklyPlan);
        eventManager.subscribe(EventType.RESET_WEEKLY_PLAN, this::handleResetWeeklyPlan);
        eventManager.subscribe(EventType.EDIT_WEEKLY_PLAN_MONDAY, (data) -> screenManager.showEditWeeklyPlanMonday());
        eventManager.subscribe(EventType.EDIT_WEEKLY_PLAN_MONDAY_SUBMIT, this::handleEditWeeklyPlanMonday);
        eventManager.subscribe(EventType.EDIT_WEEKLY_PLAN_TUESDAY, (data) -> screenManager.showEditWeeklyPlanTuesday());
        eventManager.subscribe(EventType.EDIT_WEEKLY_PLAN_TUESDAY_SUBMIT, this::handleEditWeeklyPlanTuesday);
        eventManager.subscribe(EventType.EDIT_WEEKLY_PLAN_WEDNESDAY, this::handleEditWeeklyPlanWednesday);
        eventManager.subscribe(EventType.EDIT_WEEKLY_PLAN_WEDNESDAY_SUBMIT, (data) -> screenManager.showEditWeeklyPlanWednesday());
        eventManager.subscribe(EventType.EDIT_WEEKLY_PLAN_THURSDAY, (data) -> screenManager.showEditWeeklyPlanThursday());
        eventManager.subscribe(EventType.EDIT_WEEKLY_PLAN_THURSDAY_SUBMIT, this::handleEditWeeklyPlanThursday);
        eventManager.subscribe(EventType.EDIT_WEEKLY_PLAN_FRIDAY, (data) -> screenManager.showEditWeeklyPlanFriday());
        eventManager.subscribe(EventType.EDIT_WEEKLY_PLAN_FRIDAY_SUBMIT, this::handleEditWeeklyPlanFriday);
    }

    public void showWeeklyMenu() {
        try {
            screenManager.showWeeklyMenuScreen(cantineService.isAdmin(currentUser.getUserid()));
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while validating the user. Try again!");
        }
    }

    /**
     * Handles displaying the weekly meal plan.
     * <p>
     * Retrieves the weekly plan, sorts it by day, and displays it.
     * </p>
     *
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
     *
     */
    private void handleShowEditWeeklyPlan() {
        screenManager.showEditWeeklyPlanScreen();
    }

    /**
     * Handles resetting the weekly meal plan to its default state.
     *
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
