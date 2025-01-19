package de.htwsaar.cantineplanner.businessLogic;

import de.htwsaar.cantineplanner.dataAccess.DBConnection;
import de.htwsaar.cantineplanner.presentation.TUI;

public class Controller {
    private TUI tui;
    private DBConnection dbConnection;
    private boolean running;

    public Controller() {
        this.tui = new TUI();
        this.dbConnection = new DBConnection();
        this.running = false;
    }

    public void start() {
        running = true;
        while (running) {
            menu();
        }
    }

    public void menu() {
        int choice = (int) tui.testMenue();
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
                running = false;
                System.out.println("Goodbye!");
                break;
            default:
                System.out.println("Invalid Input");
        }
    }
}

