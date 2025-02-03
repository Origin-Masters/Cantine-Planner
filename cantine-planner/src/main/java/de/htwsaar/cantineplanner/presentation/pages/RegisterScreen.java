package de.htwsaar.cantineplanner.presentation.pages;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.SGR;
import de.htwsaar.cantineplanner.businessLogic.EventManager;

import java.util.Arrays;

public class RegisterScreen extends AbstractScreen {
    private EventManager eventManager;

    public RegisterScreen(MultiWindowTextGUI gui, EventManager eventManager) {
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

        panel.addComponent(new Label("Email")
                .setForegroundColor(new TextColor.RGB(29, 29, 29))
                .addStyle(SGR.BOLD));
        TextBox email = new TextBox();
        panel.addComponent(email);

        panel.addComponent(new EmptySpace(), GridLayout.createHorizontallyFilledLayoutData(2));

        Panel buttonPanel = new Panel(new GridLayout(2));
        buttonPanel.addComponent(new Button("Close", () -> {
            gui.getActiveWindow().close();
        }));
        buttonPanel.addComponent(new Button("Register", () -> {
            String user = username.getText();
            String pass = password.getText();
            String mail = email.getText();
            if (user != null && pass != null && mail != null) {
                eventManager.notify("register", new String[]{user, pass, mail});
            } else {
                showError("All fields are required");
            }
        }));

        panel.addComponent(buttonPanel, GridLayout.createHorizontallyFilledLayoutData(2));

        BasicWindow window = new BasicWindow("Register");
        window.setComponent(panel);
        window.setHints(Arrays.asList(Window.Hint.CENTERED));

        gui.addWindowAndWait(window);
    }

    private void showError(String message) {
        ErrorScreen errorScreen = new ErrorScreen(gui, message);
        errorScreen.display();
    }

    public void showSuccess(String message) {
        SuccessScreen successScreen = new SuccessScreen(gui, message);
        successScreen.display();
    }
}