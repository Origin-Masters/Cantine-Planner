package de.htwsaar.cantineplanner.businessLogic.controller;

import de.htwsaar.cantineplanner.businessLogic.CantineService;
import de.htwsaar.cantineplanner.businessLogic.EventManager;
import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;
import de.htwsaar.cantineplanner.codegen.tables.records.UsersRecord;
import de.htwsaar.cantineplanner.exceptions.*;
import de.htwsaar.cantineplanner.presentation.ScreenManager;
import org.jooq.User;

import java.sql.SQLException;
import java.util.List;

public class MealController extends AbstractController{

    private int currentMenu;
    private boolean running;

    public MealController(ScreenManager screenManager,
                          CantineService cantineService,
                          EventManager eventManager,
                          UsersRecord userRecord) {




        super(screenManager, cantineService, eventManager, userRecord);
        this.subscribeToEvents();
    }

    @Override
    protected void subscribeToEvents() {
        // Meal-bezogene Events
        eventManager.subscribe("showAllMeals", this::handleShowAllMeals);
        eventManager.subscribe("showAllAllergies", this::handleShowAllAllergies);
        eventManager.subscribe("showAddMeal", (data) -> screenManager.showAddMealScreen());
        eventManager.subscribe("showDeleteMeal", (data) -> screenManager.showDeleteMealScreen());
        eventManager.subscribe("addMeal", this::handleAddMeal);
        eventManager.subscribe("deleteMeal", this::handleDeleteMeal);
        eventManager.subscribe("showMealDetailsById", (data) -> screenManager.showMealDetailsById());
        eventManager.subscribe("mealDetailsById", this::handleShowMealById);
        eventManager.subscribe("showSearchMealByName", (data) -> screenManager.showSearchMealByName());
        eventManager.subscribe("searchMealByName", this::handleShowMealByName);
        eventManager.subscribe("showEditMeal", (data) -> screenManager.showEditMealScreen());
        eventManager.subscribe("editMeal", this::handleEditMeal);

    }

    public void showMealMenu(int currentUserId) {
        try {
            if (cantineService.isAdmin(currentUserId)) {
                screenManager.showMealMenuScreen(cantineService.isAdmin(currentUserId));
            } else {
                screenManager.showErrorScreen("You do not have the necessary permissions to access the meal menu.");
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
    public void handleEditMeal(Object data) {
        try {
            String[] mealData = (String[]) data;

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
        } catch (MealDoesntExistException e){
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
     *
     * @param data not used
     */
    public void handleShowAllMeals(Object data) {
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
     *
     * @param data not used
     */
    private void handleShowAllAllergies(Object data) {
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
    public void handleAddMeal(Object data) {
        try {
            String[] mealData = (String[]) data;
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
    private void handleDeleteMeal(Object data) {
        try {
            String[] dataArray = (String[]) data;
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
    private void handleShowMealById(Object data) {
        try {
            String[] dataArray = (String[]) data;
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
    private void handleShowMealByName(Object data) {
        try {
            String[] dataArray = (String[]) data;
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













}
