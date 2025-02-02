package de.htwsaar.cantineplanner.tuiPresentationNew;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;

import java.util.Arrays;

public class RegisterScreen extends AbstractScreen {

    public RegisterScreen(MultiWindowTextGUI gui) {
        super(gui);
    }

    @Override
    public void display() {
        // Create a panel with a GridLayout
        Panel panel = new Panel(new GridLayout(2));
        GridLayout gridLayout = (GridLayout) panel.getLayoutManager();
        gridLayout.setHorizontalSpacing(5);
        gridLayout.setVerticalSpacing(3);

        // New Username Label and TextBox
        panel.addComponent(new Label("New Username")
                .setForegroundColor(new TextColor.RGB(29, 29, 29))
                .addStyle(SGR.BOLD));
        TextBox newUsername = new TextBox();
        panel.addComponent(newUsername);

        // New Password Label and TextBox
        panel.addComponent(new Label("New Password")
                .setForegroundColor(new TextColor.RGB(29, 29, 29))
                .addStyle(SGR.BOLD));
        TextBox newPassword = new TextBox()
                .setMask('*')
                .setSize(new TerminalSize(20, 1));
        panel.addComponent(newPassword);

        // Empty space for layout
        panel.addComponent(new EmptySpace());

        // Create a window for registration. Declared final so that it can be referenced in the lambda.
        final BasicWindow window = new BasicWindow("Register");

        // Create Account Button that simulates account creation and automatically closes the window after success
        Button createAccountButton = new Button("Create Account", () -> {
            String regUser = newUsername.getText();
            String regPass = newPassword.getText();
            // Here you would include logic to store the account details
            MessageDialog.showMessageDialog(gui, "Registration", "Account created for " + regUser, MessageDialogButton.OK);
            window.close(); // Automatically close the registration window
        });
        panel.addComponent(createAccountButton);

        // Set up the window with the panel and center it
        window.setComponent(panel);
        window.setHints(Arrays.asList(Window.Hint.CENTERED));

        // Add the window to the GUI and wait for interactions (the window will be closed after successful registration)
        gui.addWindowAndWait(window);
    }
}
