package de.htwsaar.cantineplanner.businessLogic.controller;

import de.htwsaar.cantineplanner.data.CantineService;
import de.htwsaar.cantineplanner.businessLogic.EventManager;
import de.htwsaar.cantineplanner.businessLogic.controller.eventdata.EventData;
import de.htwsaar.cantineplanner.businessLogic.controller.eventdata.EventType;
import de.htwsaar.cantineplanner.businessLogic.controller.eventdata.IntData;
import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;
import de.htwsaar.cantineplanner.exceptions.*;
import de.htwsaar.cantineplanner.presentation.ScreenManager;

import java.sql.SQLException;
import java.util.List;

/**
 * The MealController class is responsible for handling meals.
 * <p>
 * The MealController class is responsible for handling meals, including sorting meals by price, rating, name, allergens, and calories,
 * showing all meals, showing all allergies, adding a meal, deleting a meal, editing a meal, and showing meal details by ID or name.
 * </p>
 */
public class MealController extends AbstractController {

    /**
     * Constructs a new MealController.
     * <p>
     * This constructor initializes the MealController with the provided ScreenManager, CantineService, and EventManager.
     * It also subscribes to the relevant events.
     * </p>
     *
     * @param screenManager  the screen manager to manage UI screens
     * @param cantineService the service to handle cantine-related operations
     * @param eventManager   the event manager to handle events
     */
    protected MealController(ScreenManager screenManager,
                             CantineService cantineService,
                             EventManager eventManager) {
        super(screenManager, cantineService, eventManager);
        this.subscribeToEvents();
    }

    /**
     * Subscribes to various meal-related event types and associates them with their respective handlers.
     * <p>
     * This method sets up the event subscriptions for handling different meal-related events such as sorting meals,
     * showing all meals, adding a meal, deleting a meal, and editing a meal.
     * </p>
     */
    @Override
    protected void subscribeToEvents() {
        // Meal-bezogene Events
        eventManager.subscribe(EventType.SHOW_SORT_MEALS, (data) -> screenManager.showSortMealScreen());
        eventManager.subscribe(EventType.SORT_MEALS_BY_PRICE, this::handleSortMealByPrice);
        eventManager.subscribe(EventType.SORT_MEALS_BY_RATING, this::handleSortMealByRating);
        eventManager.subscribe(EventType.SORT_MEALS_BY_NAME, this::handleSortMealByName);
        eventManager.subscribe(EventType.SORT_MEALS_BY_ALLERGENS, this::handleSortMealByAllergy);
        eventManager.subscribe(EventType.SORT_MEALS_BY_CALORIES, this::handleSortMealByCalories);

        eventManager.subscribe(EventType.SHOW_ALL_MEALS, this::handleShowAllMeals);
        eventManager.subscribe(EventType.SHOW_ALL_ALLERGIES, this::handleShowAllAllergies);
        eventManager.subscribe(EventType.SHOW_DELETE_MEAL, (data) -> screenManager.showDeleteMealScreen());
        eventManager.subscribe(EventType.SHOW_ADD_MEAL, (data) -> screenManager.showAddMealScreen());
        eventManager.subscribe(EventType.ADD_MEAL, this::handleAddMeal);
        eventManager.subscribe(EventType.DELETE_MEAL, this::handleDeleteMeal);
        eventManager.subscribe(EventType.SHOW_SEARCH_MEAL_BY_ID, () -> screenManager.showMealDetailsById());
        eventManager.subscribe(EventType.SHOW_MEAL_BY_ID, this::handleShowMealById);
        eventManager.subscribe(EventType.SHOW_SEARCH_MEAL_BY_NAME, (data) -> screenManager.showSearchMealByName());
        eventManager.subscribe(EventType.SHOW_MEAL_BY_NAME, this::handleShowMealByName);
        eventManager.subscribe(EventType.SHOW_EDIT_MEAL, (data) -> screenManager.showEditMealScreen());
        eventManager.subscribe(EventType.EDIT_MEAL, this::handleEditMeal);
    }

    /**
     * Displays the meal menu screen.
     * <p>
     * This method checks if the current user is an admin and displays the meal menu screen accordingly.
     * If the user does not have the necessary permissions, an error screen is shown and the menu is switched.
     * If an error occurs while validating the user or accessing the meal menu, an error screen is shown.
     * </p>
     */
    protected void showMealMenu() {
        try {
            if (cantineService.isAdmin(currentUser.getUserid())) {
                screenManager.showMealMenuScreen(true);
            } else {
                screenManager.showErrorScreen("You do not have the necessary permissions to access the meal menu.");
                eventManager.notify(EventType.SWITCH_MENU, new IntData(1));
            }
        } catch (UseriDDoesntExcistException e) {
            screenManager.showErrorScreen("The user with the given username doesn't exist!");
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while accessing the meal menu, please try again!");
        }
    }

    /**
     * Handles editing a meal.
     * <p>
     * Validates input data and updates the meal information accordingly.
     * </p>
     *
     * @param data an Object array containing meal details; the first element must be meal ID
     */
    public void handleEditMeal(EventData data) {
        try {
            String[] mealData = (String[]) data.getData();

            // Check if at least the meal ID is provided
            if (mealData.length < 1 || mealData[0].isEmpty()) {
                screenManager.showErrorScreen("Meal ID is required!");
                return;
            }

            int mealId = Integer.parseInt(mealData[0]);
            MealsRecord meal = new MealsRecord();
            meal.setMealId(mealId);

            // If a value exists and is not empty, set it
            if (mealData.length > 1 && !mealData[1].isEmpty()) {
                meal.setName(mealData[1]);
            }
            if (mealData.length > 2 && !mealData[2].isEmpty()) {
                meal.setPrice(Float.parseFloat(mealData[2]));
            }
            if (mealData.length > 3 && !mealData[3].isEmpty()) {
                meal.setCalories(Integer.parseInt(mealData[3]));
            }
            if (mealData.length > 4 && !mealData[4].isEmpty()) {
                meal.setAllergy(mealData[4]);
            }
            if (mealData.length > 5 && !mealData[5].isEmpty()) {
                meal.setMeat(Integer.parseInt(mealData[5]));
            }

            cantineService.editMeal(meal);
            screenManager.closeActiveWindow();
            screenManager.showSuccessScreen("Meal updated successfully!");
        } catch (NumberFormatException e) {
            screenManager.showErrorScreen("Invalid input format!");
        } catch (MealDoesntExistException e) {
            screenManager.showErrorScreen(e.getMessage());
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while updating meal please try again!");
        }
    }

    /**
     * Handles the event to display all meals.
     * <p>
     * Retrieves a list of all meals and passes it to the screen manager for display.
     * </p>
     */
    public void handleShowAllMeals() {
        try {
            List<MealsRecord> meals = cantineService.getAllMeals();
            screenManager.showAllMeals(meals);
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while fetching all meals please try again!");
        }
    }

    /**
     * Handles displaying all allergy information.
     * <p>
     * Retrieves a list of all allergies from meals and displays them.
     * </p>
     */
    private void handleShowAllAllergies() {
        try {
            List<MealsRecord> allergies = cantineService.getAllAllergies();
            screenManager.showAllAllergies(allergies);
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while fetching all allergies please try again!");
        }
    }

    /**
     * Handles adding a new meal.
     * <p>
     * Parses the meal details from input data and creates a new meal record.
     * </p>
     *
     * @param data an Object array containing meal name, price, calories, allergy info, and meat flag
     */
    public void handleAddMeal(EventData data) {
        try {
            String[] mealData = (String[]) data.getData();
            String mealName = mealData[0];
            float price = Float.parseFloat(mealData[1]);
            int calories = Integer.parseInt(mealData[2]);
            String allergy = mealData[3];
            int meat = Integer.parseInt(mealData[4]);

            MealsRecord meal = new MealsRecord();
            meal.setName(mealName);
            meal.setPrice(price);
            meal.setCalories(calories);
            meal.setAllergy(allergy);
            meal.setMeat(meat);
            cantineService.addMeal(meal);
            screenManager.closeActiveWindow();
            screenManager.showSuccessScreen("Meal added successfully!");
        } catch (NumberFormatException e) {
            screenManager.showErrorScreen("Invalid input format!");
        } catch (MealAlreadyExistsException e) {
            screenManager.showErrorScreen(e.getMessage());
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while adding meal please try again!");
        }
    }

    /**
     * Handles deleting a meal.
     * <p>
     * Parses the meal ID from input data and deletes the corresponding meal.
     * </p>
     *
     * @param data an Object array where the first element is the meal ID as a String
     */
    private void handleDeleteMeal(EventData data) {
        try {
            String[] dataArray = (String[]) data.getData();
            int mealId = Integer.parseInt(dataArray[0]);
            cantineService.deleteMeal(mealId);
            screenManager.closeActiveWindow();
            screenManager.showSuccessScreen("Meal deleted successfully!");
        } catch (NumberFormatException e) {
            screenManager.showErrorScreen("Invalid meal ID format!");
        } catch (MealiDNotFoundException e) {
            screenManager.showErrorScreen(e.getMessage());
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while deleting meal please try again!");
        }
    }

    /**
     * Handles displaying meal details by meal ID.
     * <p>
     * Retrieves and displays meal details based on the provided meal ID.
     * </p>
     *
     * @param data an Object array where the first element is the meal ID as a String
     */
    private void handleShowMealById(EventData data) {
        try {
            String[] dataArray = (String[]) data.getData();
            int mealId = Integer.parseInt(dataArray[0]);
            MealsRecord meal = cantineService.getMealById(mealId);
            if (meal != null) {
                screenManager.showMealDetails(meal);
            }
        } catch (NumberFormatException e) {
            screenManager.showErrorScreen("Invalid meal ID format!");
        } catch (MealiDNotFoundException e) {
            screenManager.showErrorScreen(e.getMessage());
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while fetching meal please try again!");
        }
    }

    /**
     * Handles displaying meal details by meal name.
     * <p>
     * Retrieves and displays meal details based on the provided meal name.
     * </p>
     *
     * @param data an Object array where the first element is the meal name as a String
     */
    private void handleShowMealByName(EventData data) {
        try {
            String[] dataArray = (String[]) data.getData();
            String mealName = dataArray[0];
            MealsRecord meal = cantineService.getMealByName(mealName);
            if (meal != null) {
                screenManager.showMealDetails(meal);
            }
        } catch (MealDoesntExistException e) {
            screenManager.showErrorScreen(e.getMessage());
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while fetching meal please try again!");
        }
    }

    /**
     * Handles sorting meals by price.
     *
     * <p>This method retrieves a list of meals sorted by price from the cantine service
     * and displays them using the screen manager. If an SQLException is encountered,
     * an error screen is shown.</p>
     */
    private void handleSortMealByPrice() {
        try {
            List<MealsRecord> meals = cantineService.sortMealsByPrice();
            screenManager.showAllMeals(meals);
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while sorting meals by price please try again!");
        }
    }

    /**
     * Handles sorting meals by rating.
     *
     * <p>This method retrieves a list of meals sorted by rating from the cantine service
     * and displays them using the screen manager. If an SQLException is encountered,
     * an error screen is shown.</p>
     */
    private void handleSortMealByRating() {
        try {
            List<MealsRecord> meals = cantineService.sortMealsByRating();
            screenManager.showAllMeals(meals);
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while sorting meals by rating please try again!");
        }
    }

    /**
     * Handles sorting meals by name.
     *
     * <p>This method retrieves a list of meals sorted by name from the cantine service
     * and displays them using the screen manager. If an SQLException is encountered,
     * an error screen is shown.</p>
     */
    private void handleSortMealByName() {
        try {
            List<MealsRecord> meals = cantineService.sortMealsByName();
            screenManager.showAllMeals(meals);
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while sorting meals by name please try again!");
        }
    }

    /**
     * Handles sorting meals by calories.
     *
     * <p>This method retrieves a list of meals sorted by calories from the cantine service
     * and displays them using the screen manager. If an SQLException is encountered,
     * an error screen is shown.</p>
     */
    private void handleSortMealByCalories() {
        try {
            List<MealsRecord> meals = cantineService.sortMealsByCalories();
            screenManager.showAllMeals(meals);
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while sorting meals by calories please try again!");
        }
    }

    /**
     * Handles sorting meals by allergy.
     *
     * <p>This method retrieves a list of meals sorted by allergy information, possibly based on the current user's ID,
     * from the cantine service and displays them using the screen manager. If an SQLException is encountered,
     * an error screen is shown.</p>
     */
    private void handleSortMealByAllergy() {
        try {
            List<MealsRecord> meals = cantineService.sortMealsByAllergy(currentUser.getUserid());
            screenManager.showAllMeals(meals);
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while sorting meals by allergy please try again!");
        }
    }
}
