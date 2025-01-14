package de.htwsaar.cantineplanner.businessLogic;

import de.htwsaar.cantineplanner.presentation.TUI;

public class Controller {
    private TUI TUI;

    public Controller() {
        TUI = new TUI();
    }
    public void start(){
        if (TUI.test() != -1) {
            System.out.println("Test");
        }
    }


}
