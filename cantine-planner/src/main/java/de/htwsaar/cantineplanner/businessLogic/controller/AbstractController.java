package de.htwsaar.cantineplanner.businessLogic.controller;

import de.htwsaar.cantineplanner.businessLogic.CantineService;
import de.htwsaar.cantineplanner.businessLogic.EventManager;
import de.htwsaar.cantineplanner.codegen.tables.records.UsersRecord;
import de.htwsaar.cantineplanner.presentation.ScreenManager;

public abstract class AbstractController {
    protected ScreenManager screenManager;
    protected CantineService cantineService;
    protected EventManager eventManager;
   UsersRecord usersRecord;

    public AbstractController(ScreenManager screenManager,
                              CantineService cantineService,
                              EventManager eventManager,
                             UsersRecord usersRecord) {
        this.screenManager = screenManager;
        this.cantineService = cantineService;
        this.eventManager = eventManager;
        this.usersRecord = usersRecord;
        subscribeToEvents();
    }

    protected abstract void subscribeToEvents();

}