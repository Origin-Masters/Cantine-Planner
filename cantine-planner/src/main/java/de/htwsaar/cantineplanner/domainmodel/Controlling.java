package de.htwsaar.cantineplanner.domainmodel;

import de.htwsaar.cantineplanner.presentation.CLI;

public class Controlling {
    private CLI cli;

    public Controlling() {
        cli = new CLI();
    }
    public void start(){
        cli.chooseAction();
    }


}
