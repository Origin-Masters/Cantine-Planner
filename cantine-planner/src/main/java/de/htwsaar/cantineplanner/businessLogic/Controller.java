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
            case 1:
               dbConnection.allReviews();
                break;
            case 2:
                System.out.println("Cumming soon");
                break;
            case 3:
                System.out.println("Cumming soon");
                break;
            case 4:
                System.out.println("Cumming soon");
                break;
            case 5:
                System.out.println("Cumming soon");
                break;
            case 6:
                System.out.println("Cumming soon");
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

