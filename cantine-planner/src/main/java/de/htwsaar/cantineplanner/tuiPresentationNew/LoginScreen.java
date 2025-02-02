package de.htwsaar.cantineplanner.tuiPresentationNew;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;

import java.util.Arrays;

public class LoginScreen extends AbstractScreen {

    public LoginScreen(MultiWindowTextGUI gui) {
        super(gui);
    }

    @Override
    public void display() {
        // Create the panel with a GridLayout
        Panel panel = new Panel(new GridLayout(2));
        GridLayout gridLayout = (GridLayout) panel.getLayoutManager();
        gridLayout.setHorizontalSpacing(5);
        gridLayout.setVerticalSpacing(3);

        // Username Label and TextBox
        panel.addComponent(new Label("Username")
                .setForegroundColor(new TextColor.RGB(29, 29, 29))
                .addStyle(SGR.BOLD));
        TextBox username = new TextBox();
        panel.addComponent(username);

        // Password Label and TextBox
        panel.addComponent(new Label("Password")
                .setForegroundColor(new TextColor.RGB(29, 29, 29))
                .addStyle(SGR.BOLD));
        TextBox password = new TextBox()
                .setMask('*') // Mask input with asterisks
                .setSize(new TerminalSize(20, 1)); // Set size to 20 columns and 1 row
        panel.addComponent(password);

        // Add an empty space for layout purposes
        panel.addComponent(new EmptySpace());

        // Login Button with logic for valid login or redirection to registration
        Button loginButton = new Button("Login", () -> {
            String user = username.getText();
            String pass = password.getText();
            // Dummy login check: only "admin" / "admin" is valid
            if (user.equals("admin") && pass.equals("admin")) {
                MessageDialog.showMessageDialog(gui, "Login Successful", "Welcome " + user, MessageDialogButton.OK);
            } else {
                // If login fails, ask the user if they want to register
                MessageDialogButton response = MessageDialog.showMessageDialog(gui, "Login Failed",
                        "Invalid credentials. Would you like to register a new account?",
                        MessageDialogButton.Yes, MessageDialogButton.No);
                if (response == MessageDialogButton.Yes) {
                    // Redirect to the registration screen
                    RegisterScreen registerScreen = new RegisterScreen(gui);
                    registerScreen.display();
                }
            }
        });
        panel.addComponent(loginButton);

        // Additionally, you can include a dedicated "Register" button.
        Button registerButton = new Button("Register", () -> {
            RegisterScreen registerScreen = new RegisterScreen(gui);
            registerScreen.display();
        });
        panel.addComponent(registerButton);

        // Create a window and set the panel as its content
        BasicWindow window = new BasicWindow("Login");
        window.setComponent(panel);
        window.setHints(Arrays.asList(Window.Hint.CENTERED));

        // Add the window to the GUI and wait for user interactions
        gui.addWindowAndWait(window);
    }
}
