package de.htwsaar.cantineplanner.presentation;

import de.htwsaar.cantineplanner.data.CantineService;
import de.htwsaar.cantineplanner.businessLogic.EventManager;
import de.htwsaar.cantineplanner.businessLogic.controller.MainController;
import de.htwsaar.cantineplanner.dbUtils.DataBaseUtil;


public class App {
    public static void main(String[] args) {
        DataBaseUtil.loadInitialDataBase("./database/database.db");


        String PATH_TO_PROPERTIES = "hikari.properties";

        EventManager eventManager = new EventManager();
        CantineService cantineService = new CantineService(PATH_TO_PROPERTIES);
        ScreenManager screenManager = new ScreenManager(eventManager, cantineService);


       // Instantiate and start the MainController:
        MainController mainController = new MainController(
             screenManager,
               cantineService,
              eventManager
       );

       mainController.start();


        //Controller controller = new Controller();
        //controller.start();





    }
}