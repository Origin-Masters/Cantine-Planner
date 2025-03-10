package de.htwsaar.cantineplanner.businessLogic.controller;

import de.htwsaar.cantineplanner.businessLogic.CantineService;
import de.htwsaar.cantineplanner.businessLogic.EventManager;
import de.htwsaar.cantineplanner.presentation.ScreenManager;

public abstract class AbstractController {
    protected ScreenManager screenManager;
    protected CantineService cantineService;
    protected EventManager eventManager;
    protected int currentUserId;

    public AbstractController(ScreenManager screenManager,
                              CantineService cantineService,
                              EventManager eventManager,
                              int currentUserId) {
        this.screenManager = screenManager;
        this.cantineService = cantineService;
        this.eventManager = eventManager;
        this.currentUserId = currentUserId;
        subscribeToEvents();
    }

    protected abstract void subscribeToEvents();

}