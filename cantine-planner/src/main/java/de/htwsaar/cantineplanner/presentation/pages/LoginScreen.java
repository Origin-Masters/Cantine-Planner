// src/main/java/de/htwsaar/cantineplanner/presentation/pages/LoginScreen.java
package de.htwsaar.cantineplanner.presentation.pages;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.SGR;
import de.htwsaar.cantineplanner.businessLogic.EventManager;

import java.util.Arrays;

public class LoginScreen extends AbstractScreen {
    private EventManager eventManager;

    public LoginScreen(MultiWindowTextGUI gui, EventManager eventManager) {
        super(gui);
        this.eventManager = eventManager;
    }

    @Override
    public void display() {
        Panel panel = new Panel(new GridLayout(2));
        GridLayout gridLayout = (GridLayout) panel.getLayoutManager();
        gridLayout.setHorizontalSpacing(5);
        gridLayout.setVerticalSpacing(3);

        panel.addComponent(new Label("Username")
                .setForegroundColor(new TextColor.RGB(29, 29, 29))
                .addStyle(SGR.BOLD));
        TextBox username = new TextBox();
        panel.addComponent(username);

        panel.addComponent(new Label("Password")
                .setForegroundColor(new TextColor.RGB(29, 29, 29))
                .addStyle(SGR.BOLD));
        TextBox password = new TextBox()
                .setMask('*')
                .setSize(new TerminalSize(20, 1));
        panel.addComponent(password);

        panel.addComponent(new EmptySpace());

        Button loginButton = new Button("Login", () -> {
            String user = username.getText();
            String pass = password.getText();
            eventManager.notify("login", new String[]{user, pass});
        });
        panel.addComponent(loginButton);

        Button registerButton = new Button("Register", () -> {
            eventManager.notify("showRegisterScreen", null);
        });
        panel.addComponent(registerButton);

        BasicWindow window = new BasicWindow("Login");
        window.setComponent(panel);
        window.setHints(Arrays.asList(Window.Hint.CENTERED));

        gui.addWindowAndWait(window);
    }
}