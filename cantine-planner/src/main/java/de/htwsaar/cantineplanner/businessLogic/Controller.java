package de.htwsaar.cantineplanner.businessLogic;

import de.htwsaar.cantineplanner.dataAccess.DBConnection;
import de.htwsaar.cantineplanner.presentation.TUI;

public class Controller {
    private TUI tui;
    private DBConnection dbConnection;

    public Controller() {
        this.tui = new TUI();
        this.dbConnection = new DBConnection();

    }
    public void start(){
        int choice = (int) tui.test();
        switch (choice){
            case 1:
                dbConnection.allMeals();
                break;
            case 2:
                dbConnection.createMeal(tui.createMeal());
                break;

            case 3 :
                dbConnection.allAllergies();

            default:
                System.out.println("Invalid Input");
        }
    }





}
