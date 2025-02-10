package de.htwsaar.cantineplanner.businessLogic;

import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;
import de.htwsaar.cantineplanner.codegen.tables.records.ReviewRecord;
import de.htwsaar.cantineplanner.exceptions.*;
import de.htwsaar.cantineplanner.presentation.ScreenManager;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class Controller {

    // Abhängigkeiten und Statusvariablen
    private ScreenManager screenManager;
    private CantineService cantineService;
    private EventManager eventManager;
    private boolean running;
    private int currentMenu;
    private int currentUserId;

    public Controller() {
        this.eventManager = new EventManager();
        this.screenManager = new ScreenManager(eventManager);
        this.cantineService = new CantineService();
        this.currentUserId = -1;
        this.running = false;
        subscribeToEvents();
    }

    /**
     * Registriert alle Events – gruppiert nach Themenbereichen.
     */
    private void subscribeToEvents() {
        // Navigation und allgemeine Nachrichten
        eventManager.subscribe("showMainMenu", (data) -> switchMenu(1));
        eventManager.subscribe("showMealMenu", (data) -> switchMenu(2));
        eventManager.subscribe("showReviewMenu", (data) -> switchMenu(3));
        eventManager.subscribe("showUserMenu", (data) -> switchMenu(4));
        eventManager.subscribe("showWeeklyMenu", (data) -> switchMenu(5));
        eventManager.subscribe("exit", (data) -> exitApplication());
        eventManager.subscribe("success", (data) -> screenManager.showSuccessScreen((String) data));
        eventManager.subscribe("error", (data) -> screenManager.showErrorScreen((String) data));

        // User-Authentifizierung und Registrierung
        eventManager.subscribe("login", this::handleLogin);
        eventManager.subscribe("register", this::handleRegister);
        eventManager.subscribe("showRegisterScreen", this::handleShowRegisterScreen);
        eventManager.subscribe("logout", (data) -> {
            switchMenu(0);
            this.currentUserId = -1;
        });

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

        // Review-bezogene Events
        eventManager.subscribe("showAddReview", (data) -> screenManager.showAddReviewScreen());
        eventManager.subscribe("addReview", this::handleAddReview);
        eventManager.subscribe("showDeleteReview", (data) -> screenManager.showDeleteReviewScreen());
        eventManager.subscribe("deleteReview", this::handleDeleteReview);
        eventManager.subscribe("showAllReviews", this::handleShowAllReviews);
        eventManager.subscribe("showSearchReviewsByMealId", (data) -> screenManager.showSearchReviewsByMealId());
        eventManager.subscribe("searchReviewsByMealId", this::handleSearchReviewsByMealId);

        eventManager.subscribe("showSearchReviewsByMealName", (data) -> screenManager.showSearchReviewsByMealName());
        eventManager.subscribe("searchReviewsByMealName", this::handleSearchReviewsByMealName);

        // User-bezogene Events
        eventManager.subscribe("showReviewsByUser", this::handleShowReviewsByUser);
    }

    // Hauptschleife
    public void start() {
        running = true;
        currentMenu = 0;
        while (running) {
            switch (currentMenu) {
                case 0: loginMenu(); break;
                case 1: mainMenu(); break;
                case 2: mealMenu(); break;
                case 3: reviewMenu(); break;
                case 4: userMenu(); break;
                case 5: weeklyMenu(); break;
                default: System.out.println("Invalid Input");
            }
        }
    }

    // Menü-Navigation
    private void switchMenu(int menu) {
        screenManager.closeActiveWindow();
        currentMenu = menu;
    }
    public void loginMenu() {
        screenManager.showLoginScreen();
    }
    public void mainMenu() {
        screenManager.showMainMenuScreen();
    }
    public void mealMenu() {
        screenManager.showMealMenuScreen();
    }
    public void reviewMenu() {
        screenManager.showReviewsMenu();
    }
    public void userMenu() {
        screenManager.showUserMenuScreen();
    }

    public void weeklyMenu() {
        screenManager.showWeeklyMenuScreen();
    }

    // Beenden
    private void exitApplication() {
        screenManager.closeTerminal();
        running = false;
    }

    // --- Event-Handler ---

    // User-Handler
    public void handleLogin(Object data) {
        String[] credentials = (String[]) data;
        String username = credentials[0];
        String password = credentials[1];
        try {
            if (cantineService.validateUser(username, password)) {
                currentUserId = cantineService.getUserId(username);
                screenManager.closeActiveWindow();
                screenManager.showSuccessScreen("Login successful!");
                switchMenu(1);
            } else {
                screenManager.showErrorScreen("Username or password is incorrect. Please retry!");
            }
        } catch (UserNotValidatedException | UserAlreadyExistsException e) {
            screenManager.showErrorScreen(e.getMessage());
        } catch (SQLException | NullPointerException e) {
            screenManager.showErrorScreen("There was an error while logging in please try again!");
        }
    }

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
                switchMenu(0);
            } else {
                screenManager.showErrorScreen("Registration failed");
            }
        } catch (SQLException | UserAlreadyExistsException e) {
            screenManager.showErrorScreen("There was an error while registering please try again!");
        }
    }

    private void handleShowRegisterScreen(Object data) {
        screenManager.showInputScreenReg("Register", "register");
    }

    private boolean isAnyFieldEmpty(String... fields) {
        for (String field : fields) {
            if (Objects.equals(field, "")) {
                return true;
            }
        }
        return false;
    }

    // Meal-Handler
    public void handleShowAllMeals(Object data) {
        try {
            List<MealsRecord> meals = cantineService.getAllMeals();
            screenManager.showAllMeals(meals);
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while fetching all meals please try again!");
        }
    }

    private void handleShowAllAllergies(Object data) {
        try {
            List<MealsRecord> allergies = cantineService.getAllAllergies();
            screenManager.showAllAllergies(allergies);
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while fetching all allergies please try again!");
        }
    }

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

    private void handleDeleteMeal(Object data) {
        try {
            String[] dataArray = (String[]) data;
            int mealId = Integer.parseInt(dataArray[0]);
            cantineService.deleteMeal(mealId);
            screenManager.closeActiveWindow();
            screenManager.showSuccessScreen("Meal deleted successfully!");
        } catch (NumberFormatException e) {
            screenManager.showErrorScreen("Invalid meal ID format!");
        } catch (MealDoesntExistException e) {
            screenManager.showErrorScreen(e.getMessage());
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while deleting meal please try again!");
        }
    }

    private void handleShowMealById(Object data) {
        try {
            String[] dataArray = (String[]) data;
            int mealId = Integer.parseInt(dataArray[0]);
            MealsRecord meal = cantineService.getMealById(mealId);
            if (meal != null) {
                screenManager.showMealDetails(meal);
            } else {
                screenManager.showErrorScreen("Meal not found!");
            }
        } catch (NumberFormatException e) {
            screenManager.showErrorScreen("Invalid meal ID format!");
        } catch (MealDoesntExistException e) {
            screenManager.showErrorScreen(e.getMessage());
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while fetching meal please try again!");
        }
    }

    private void handleShowMealByName(Object data) {
        try {
            String[] dataArray = (String[]) data;
            String mealName = dataArray[0];
            MealsRecord meal = cantineService.getMealByName(mealName);
            if (meal != null) {
                screenManager.showMealDetails(meal);
            } else {
                screenManager.showErrorScreen("Meal " + mealName + " not found!");
            }
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while fetching meal please try again!");
        }
    }

    // Review-Handler
    public void handleAddReview(Object data) {
        try {
            String[] reviewData = (String[]) data;
            int mealId = Integer.parseInt(reviewData[2]);
            int rating = Integer.parseInt(reviewData[0]);
            String comment = reviewData[1];

            ReviewRecord review = new ReviewRecord();
            review.setMealId(mealId);
            review.setRating(rating);
            review.setComment(comment);
            review.setUserid(currentUserId);
            cantineService.addReview(review);
            screenManager.closeActiveWindow();
            screenManager.showSuccessScreen("Review added successfully!");
        } catch (NumberFormatException e) {
            screenManager.showErrorScreen("Invalid input format!");
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while adding review please try again!");
        }
    }

    private void handleDeleteReview(Object data) {
        try {
            String[] dataArray = (String[]) data;
            int ratingId = Integer.parseInt(dataArray[0]);
            cantineService.deleteReview(ratingId);
            screenManager.closeActiveWindow();
            screenManager.showSuccessScreen("Review deleted successfully!");
        } catch (NumberFormatException e) {
            screenManager.showErrorScreen("Invalid review ID format!");
        } catch (ReviewiDDoesntExistException e) {
            screenManager.showErrorScreen(e.getMessage());
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while deleting review please try again!");
        }
    }

    private void handleShowAllReviews(Object data) {
        try {
            List<ReviewRecord> reviews = cantineService.getAllReviews();
            screenManager.showAllReviews(reviews);
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while showing all Reviews please try again!");
        }
    }

    private void handleSearchReviewsByMealId(Object data) {
        try {
            String[] dataArray = (String[]) data;
            int mealId = Integer.parseInt(dataArray[0]);
            List<ReviewRecord> reviews = cantineService.searchReviewsByMealId(mealId);
            screenManager.showAllReviews(reviews);
        } catch (NumberFormatException e) {
            screenManager.showErrorScreen("Invalid meal ID format!");
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while searching for the Reviews please try again!");
        }
    }

    private void handleSearchReviewsByMealName(Object data) {
        try {
            String[] dataArray = (String[]) data;
            String mealName = dataArray[0];
            List<ReviewRecord> reviews = cantineService.searchReviewsByMealName(mealName);
            screenManager.showAllReviews(reviews);
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while searching for the Reviews please try again!");
        }
    }


    // User-Handler
    private void handleShowReviewsByUser(Object data) {
        try {
            List<ReviewRecord> reviews = cantineService.getAllReviewsByUser(currentUserId);
            screenManager.showAllReviews(reviews);
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while fetching all reviews please try again!");
        }
    }
}
