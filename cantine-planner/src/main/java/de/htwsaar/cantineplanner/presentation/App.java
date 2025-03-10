package de.htwsaar.cantineplanner.presentation;

import de.htwsaar.cantineplanner.businessLogic.Controller;
import de.htwsaar.cantineplanner.dbUtils.DataBaseUtil;


public class App {
    public static void main(String[] args) {
        DataBaseUtil.loadInitialDataBase("./database/database.db");
        Controller controller = new Controller();
        controller.start();

    }
}