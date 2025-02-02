package de.htwsaar.cantineplanner.tuiPresentationNew;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;

public class LoginScreen {
    private final MultiWindowTextGUI gui;

    public LoginScreen(MultiWindowTextGUI gui) {
        this.gui = gui;
    }

    public void display() {
        // Create a panel for login
        Panel panel = new Panel();
        panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        panel.addComponent(new Label("Please log in"));

        panel.addComponent(new Label("Username:"));
        TextBox username = new TextBox();
        panel.addComponent(username);

        panel.addComponent(new Label("Password:"));
        // Password field (masked with '*')
        TextBox password = new TextBox().setMask('*');
        panel.addComponent(password);

        // Login button
        panel.addComponent(new Button("Login", () -> {
            // Example credential check
            if ("user".equals(username.getText()) && "pass".equals(password.getText())) {
                // On successful login, close the current window and navigate to the main menu
                gui.getActiveWindow().close();
                MainMenuScreen mainMenu = new MainMenuScreen(gui);
                mainMenu.display();
            } else {
                // Show error message
                MessageDialog.showMessageDialog(gui, "Error", "Invalid login credentials", MessageDialogButton.OK);
            }
        }));

        // Create a window and add the panel
        BasicWindow window = new BasicWindow("Login");
        window.setComponent(panel);
        gui.addWindowAndWait(window);
    }
}