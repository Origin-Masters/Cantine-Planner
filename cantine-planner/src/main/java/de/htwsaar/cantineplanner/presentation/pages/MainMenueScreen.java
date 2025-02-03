// src/main/java/de/htwsaar/cantineplanner/presentation/pages/MenuScreen.java
package de.htwsaar.cantineplanner.presentation.pages;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.SGR;
import de.htwsaar.cantineplanner.businessLogic.EventManager;

import java.util.Arrays;

public class MainMenueScreen extends AbstractScreen {
    private EventManager eventManager;

    public MainMenueScreen(MultiWindowTextGUI gui, EventManager eventManager) {
        super(gui);
        this.eventManager = eventManager;
    }

    @Override
    public void display() {
        Panel panel = new Panel(new GridLayout(1));
        GridLayout gridLayout = (GridLayout) panel.getLayoutManager();
        gridLayout.setHorizontalSpacing(5);
        gridLayout.setVerticalSpacing(3);

        panel.addComponent(new Label("Main Menu")
                .setForegroundColor(new TextColor.RGB(29, 29, 29))
                .addStyle(SGR.BOLD));

        Button userMenuButton = new Button("User Menu", () -> {
            eventManager.notify("showUserMenu", null);
        });
        panel.addComponent(userMenuButton);

        Button mealMenuButton = new Button("Meal Menu", () -> {
            eventManager.notify("showMealMenu", null);
        });
        panel.addComponent(mealMenuButton);

        Button reviewMenuButton = new Button("Review Menu", () -> {
            eventManager.notify("showReviewMenu", null);
        });
        panel.addComponent(reviewMenuButton);

        Button exitButton = new Button("Exit", () -> {
            eventManager.notify("exit", null);
        });
        panel.addComponent(exitButton);

        BasicWindow window = new BasicWindow("Menu");
        window.setComponent(panel);
        window.setHints(Arrays.asList(Window.Hint.CENTERED));

        gui.addWindowAndWait(window);
    }
}