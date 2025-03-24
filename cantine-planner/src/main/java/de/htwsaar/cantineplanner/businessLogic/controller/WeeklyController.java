package de.htwsaar.cantineplanner.businessLogic.controller;

import de.htwsaar.cantineplanner.businessLogic.CantineService;
import de.htwsaar.cantineplanner.businessLogic.EventManager;
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

        eventManager.subscribe("showWeeklyPlan", this::handleShowWeeklyPlan);
        eventManager.subscribe("editWeeklyPlan", this::handleShowEditWeeklyPlan);
        eventManager.subscribe("resetWeeklyPlan", this::handleResetWeeklyPlan);
        eventManager.subscribe("editWeeklyPlanMonday", (data) -> screenManager.showEditWeeklyPlanMonday());
        eventManager.subscribe("editWeeklyPlanMondaySubmit", this::handleEditWeeklyPlanMonday);
        eventManager.subscribe("editWeeklyPlanTuesday", (data) -> screenManager.showEditWeeklyPlanTuesday());
        eventManager.subscribe("editWeeklyPlanTuesdaySubmit", this::handleEditWeeklyPlanTuesday);
        eventManager.subscribe("editWeeklyPlanWednesday", (data) -> screenManager.showEditWeeklyPlanWednesday());
        eventManager.subscribe("editWeeklyPlanWednesdaySubmit", this::handleEditWeeklyPlanWednesday);
        eventManager.subscribe("editWeeklyPlanThursday", (data) -> screenManager.showEditWeeklyPlanThursday());
        eventManager.subscribe("editWeeklyPlanThursdaySubmit", this::handleEditWeeklyPlanThursday);
        eventManager.subscribe("editWeeklyPlanFriday", (data) -> screenManager.showEditWeeklyPlanFriday());
        eventManager.subscribe("editWeeklyPlanFridaySubmit", this::handleEditWeeklyPlanFriday);
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
     * @param data not used
     */
    private void handleShowWeeklyPlan(Object data) {
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
     * @param data not used
     */
    private void handleShowEditWeeklyPlan(Object data) {
        screenManager.showEditWeeklyPlanScreen();
    }

    /**
     * Handles resetting the weekly meal plan to its default state.
     *
     * @param data not used
     */
    private void handleResetWeeklyPlan(Object data) {
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
    public void handleEditWeeklyPlanMonday(Object data) {
        String[] mealData = (String[]) data;
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
    public void handleEditWeeklyPlanTuesday(Object data) {
        String[] mealData = (String[]) data;
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
    public void handleEditWeeklyPlanWednesday(Object data) {
        String[] mealData = (String[]) data;
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
    public void handleEditWeeklyPlanThursday(Object data) {
        String[] mealData = (String[]) data;
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
    public void handleEditWeeklyPlanFriday(Object data) {
        String[] mealData = (String[]) data;
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
