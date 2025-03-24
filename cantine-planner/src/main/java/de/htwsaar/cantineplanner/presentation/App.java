package de.htwsaar.cantineplanner.presentation;

import de.htwsaar.cantineplanner.businessLogic.CantineService;
//import de.htwsaar.cantineplanner.businessLogic.Controller;
import de.htwsaar.cantineplanner.businessLogic.Controller;
import de.htwsaar.cantineplanner.businessLogic.EventManager;
import de.htwsaar.cantineplanner.businessLogic.controller.MainController;
import de.htwsaar.cantineplanner.codegen.tables.records.UsersRecord;
import de.htwsaar.cantineplanner.dbUtils.DataBaseUtil;


public class App {
    public static void main(String[] args) {
        DataBaseUtil.loadInitialDataBase("./database/database.db");


        String PATH_TO_PROPERTIES = "hikari.properties";

//        EventManager eventManager = new EventManager();
//        CantineService cantineService = new CantineService(PATH_TO_PROPERTIES);
//        ScreenManager screenManager = new ScreenManager(eventManager, cantineService);
//
//        UsersRecord newUser = new UsersRecord();
//        newUser.setUsername("admin");
//        newUser.setPassword("admin");
//        newUser.setEmail("admin@htwsaar.de");
//
//
//        // Instantiate and start the MainController:
//        MainController mainController = new MainController(
//                screenManager,
//                cantineService,
//                eventManager,
//                new UsersRecord()
//        );
//
//        mainController.start();


        Controller controller = new Controller();
        controller.start();





    }
}