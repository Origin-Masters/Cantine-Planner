package de.htwsaar.cantineplanner.businessLogic.controller;

import de.htwsaar.cantineplanner.businessLogic.manager.SessionManager;
import de.htwsaar.cantineplanner.businessLogic.service.CantineService;
import de.htwsaar.cantineplanner.businessLogic.manager.EventManager;
import de.htwsaar.cantineplanner.presentation.ScreenManager;

public abstract class AbstractController {
    protected final ScreenManager screenManager;
    protected final CantineService cantineService;
    protected final EventManager eventManager;
    protected final SessionManager sessionManager;

    public AbstractController(ScreenManager screenManager,
                              CantineService cantineService,
                              EventManager eventManager,
                              SessionManager sessionManager) {
        this.screenManager = screenManager;
        this.cantineService = cantineService;
        this.eventManager = eventManager;
        this.sessionManager = sessionManager;
    }

    protected abstract void subscribeToEvents();
}