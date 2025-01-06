package de.htwsaar.cantineplanner.presentation;

import de.htwsaar.cantineplanner.domainmodel.Controlling;

public class App {
    public static void main(String[] args) {
        Controlling controlling = new Controlling();
        controlling.start();
    }
}