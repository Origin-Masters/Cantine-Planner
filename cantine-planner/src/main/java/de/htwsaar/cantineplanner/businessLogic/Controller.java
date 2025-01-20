package de.htwsaar.cantineplanner.businessLogic;

import de.htwsaar.cantineplanner.dataAccess.DBConnection;
import de.htwsaar.cantineplanner.presentation.TUI;

public class Controller {
    private TUI tui;
    private DBConnection dbConnection;
    private boolean running;
    private int currentMenu;


    public Controller() {
        this.tui = new TUI();
        this.dbConnection = new DBConnection();
        this.running = false;
    }

    public void start() {
        running = true;
        currentMenu = 0;
        while (running) {
            switch (currentMenu) {
                case 0:
                    mainMenu();
                    break;
                case 1:
                    mealMenue();
                    break;
                case 2:
                    reviewMenue();
                    break;
                default:
                    System.out.println("Invalid Input");
            }
        }
    }

    public void mainMenu() {
        int choice = (int) tui.mainMenue();
        switch (choice) {
            case 1:
                currentMenu = 1;
                break;
            case 2:
                currentMenu = 2;
                break;
            case 3:
                running = false;
                System.out.println("Goodbye!");
                break;
            default:
                System.out.println("Invalid Input");
        }
    }

    public void reviewMenue(){
        int choice = (int) tui.reviewMenue();
        switch (choice) {
                // show all Reviews
            case 1:
               dbConnection.allReviews();
                break;
                // add Review
            case 2:

                dbConnection.addReview(tui.createReview());
                break;
                // delete Review
            case 3:

                dbConnection.deleteReview(tui.searchReview());
                break;
                // Look for Review via meal iD
            case 4:
               dbConnection.reviewByMealiD(tui.searchMealById());
                break;
                // Show All Reviews per Meal Name
            case 5:
                dbConnection.reviewsByMealName(tui.searchMeal());
                break;
                // show main menu
            case 6:
                currentMenu = 0;
                break;
                // quit programme
            case 7:
                running = false;
                System.out.println("Goodbye!");
                break;
            default:
                System.out.println("Invalid Input");
        }
    }

    public void mealMenue() {
        int choice = (int) tui.mealMenue();
        switch (choice) {
            case 1:
                dbConnection.allMeals();
                break;
            case 2:
                dbConnection.addMeal(tui.createMeal());
                break;
            case 3:
                dbConnection.allAllergies();
                break;
            case 4:
                dbConnection.deleteMeal(tui.deleteMeal());
                break;
            case 5:
                dbConnection.searchMeal(tui.searchMeal());
                break;
            case 6:
                dbConnection.mealDetails(tui.searchMealById());
                break;
            case 7:
                currentMenu = 0;
                break;
            case 8:
                running = false;
                System.out.println("Goodbye!");
                break;
            default:
                System.out.println("Invalid Input");
        }
    }
}

