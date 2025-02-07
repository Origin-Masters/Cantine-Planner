// src/main/java/de/htwsaar/cantineplanner/presentation/Main.java
package de.htwsaar.cantineplanner.presentation;

import de.htwsaar.cantineplanner.businessLogic.Controller;

public class App {
    public static void main(String[] args) {

        Controller controller = new Controller();
        controller.start();
    }
}