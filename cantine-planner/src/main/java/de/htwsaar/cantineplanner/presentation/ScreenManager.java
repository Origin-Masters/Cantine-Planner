// src/main/java/de/htwsaar/cantineplanner/presentation/ScreenManager.java
package de.htwsaar.cantineplanner.presentation;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import de.htwsaar.cantineplanner.businessLogic.EventManager;
import de.htwsaar.cantineplanner.presentation.pages.*;

import java.io.IOException;

public class ScreenManager {
    private MultiWindowTextGUI gui;

    public ScreenManager() {
        try {
            Screen screen = new DefaultTerminalFactory().createScreen();
            screen.startScreen();
            this.gui = new MultiWindowTextGUI(screen);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showLoginScreen(EventManager eventManager) {
        LoginScreen loginScreen = new LoginScreen(gui, eventManager);
        loginScreen.display();
    }

    public void showRegisterScreen(EventManager eventManager) {
        RegisterScreen registerScreen = new RegisterScreen(gui, eventManager);
        registerScreen.display();
    }

    public void showErrorScreen(String message) {
        ErrorScreen errorScreen = new ErrorScreen(gui, message);
        errorScreen.display();
    }

    public void showSuccessScreen(String message) {
        SuccessScreen successScreen = new SuccessScreen(gui, message);
        successScreen.display();
    }

    public void showMenuScreen(EventManager eventManager) {
        MainMenueScreen  mainMenueScreen= new MainMenueScreen(gui, eventManager);
        mainMenueScreen.display();
    }

    public void closeActiveWindow() {
        if (gui.getActiveWindow() != null) {
            gui.getActiveWindow().close();
        }
    }
    public void closeTerminal() throws IOException {
        gui.getScreen().stopScreen();
    }
}