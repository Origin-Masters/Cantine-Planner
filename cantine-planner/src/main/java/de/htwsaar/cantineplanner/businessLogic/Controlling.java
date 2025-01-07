package de.htwsaar.cantineplanner.businessLogic;

import de.htwsaar.cantineplanner.presentation.TUI;

public class Controlling {
    private TUI TUI;

    public Controlling() {
        TUI = new TUI();
    }
    public void start(){
        TUI.chooseAction();
    }


}
