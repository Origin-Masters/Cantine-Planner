package de.htwsaar.cantineplanner.tuiPresentationNew;

import com.googlecode.lanterna.gui2.*;

public class MainMenuScreen extends AbstractScreen {

    public MainMenuScreen(MultiWindowTextGUI gui) {
        super(gui);
    }

    @Override
    public void display() {
        // Create a panel for the main menu
        Panel panel = new Panel();
        panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        panel.addComponent(new Label("Main Menu"));

        panel.addComponent(new Button("Meals", () -> {
            // On click, close the current window and navigate to the meal menu
            gui.getActiveWindow().close();
            // MealScreen mealScreen = new MealScreen(gui);
            //mealScreen.display();
        }));

        panel.addComponent(new Button("Reviews", () -> {
            // On click, close the current window and navigate to the review menu
            gui.getActiveWindow().close();
            // ReviewScreen reviewScreen = new ReviewScreen(gui);
            //reviewScreen.display();
        }));

        panel.addComponent(new Button("Logout", () -> {
            // On click, close the current window and navigate to the login screen
            gui.getActiveWindow().close();
            LoginScreen loginScreen = new LoginScreen(gui);
            loginScreen.display();
        }));

        // Create a window and add the panel
        BasicWindow window = new BasicWindow("Main Menu");
        window.setComponent(panel);
        gui.addWindowAndWait(window);
    }
}