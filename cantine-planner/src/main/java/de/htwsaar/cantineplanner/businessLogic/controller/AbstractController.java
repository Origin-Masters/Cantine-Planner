package de.htwsaar.cantineplanner.businessLogic.controller;

import de.htwsaar.cantineplanner.data.CantineService;
import de.htwsaar.cantineplanner.businessLogic.EventManager;
import de.htwsaar.cantineplanner.codegen.tables.records.UsersRecord;
import de.htwsaar.cantineplanner.presentation.ScreenManager;

public abstract class AbstractController {
    protected ScreenManager screenManager;
    protected CantineService cantineService;
    protected EventManager eventManager;
    protected static UsersRecord currentUser;

    public AbstractController(ScreenManager screenManager,
                              CantineService cantineService,
                              EventManager eventManager) {
        this.screenManager = screenManager;
        this.cantineService = cantineService;
        this.eventManager = eventManager;
    }
    protected abstract void subscribeToEvents();

}