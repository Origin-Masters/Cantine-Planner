package de.htwsaar.cantineplanner.businessLogic.controller;

import de.htwsaar.cantineplanner.data.CantineService;
import de.htwsaar.cantineplanner.businessLogic.EventManager;
import de.htwsaar.cantineplanner.codegen.tables.records.UsersRecord;
import de.htwsaar.cantineplanner.presentation.ScreenManager;

public abstract class AbstractController {
    protected static  ScreenManager screenManager;
    protected static  CantineService cantineService;
    protected static  EventManager eventManager;
    protected static UsersRecord currentUser;

    public AbstractController(ScreenManager screenManager,
                              CantineService cantineService,
                              EventManager eventManager) {
        AbstractController.screenManager = screenManager;
        AbstractController.cantineService = cantineService;
        AbstractController.eventManager = eventManager;
    }

    public AbstractController() {

    }
    protected abstract void subscribeToEvents();

}