package de.htwsaar.cantineplanner.presentation.pages;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import de.htwsaar.cantineplanner.businessLogic.EventManager;

import java.util.Arrays;

public class MealMenuScreen extends AbstractScreen {
    private EventManager eventManager;

    public MealMenuScreen(MultiWindowTextGUI gui, EventManager eventManager) {
        super(gui);
        this.eventManager = eventManager;
    }

    @Override
    public void display() {
        Panel panel = new Panel(new GridLayout(1));
        GridLayout gridLayout = (GridLayout) panel.getLayoutManager();
        gridLayout.setHorizontalSpacing(3);
        gridLayout.setVerticalSpacing(3);

        panel.addComponent(createButton("Alle Gerichte anzeigen", "showAllMeals"));
        panel.addComponent(createButton("Gericht hinzufügen", "addMeal"));
        panel.addComponent(createButton("Alle Allergien anzeigen", "showAllAllergies"));
        panel.addComponent(createButton("Gericht löschen", "deleteMeal"));
        panel.addComponent(createButton("Gericht nach Name suchen", "searchMealByName"));
        panel.addComponent(createButton("Gerichtdetails nach ID anzeigen", "showMealDetailsById"));
        panel.addComponent(createButton("Main Menü", "showMainMenu"));
        panel.addComponent(createButton("Programm beenden", "exit"));

        BasicWindow window = new BasicWindow("Meal Menü");
        window.setComponent(panel);
        window.setHints(Arrays.asList(Window.Hint.CENTERED));

        gui.addWindowAndWait(window);
    }

    private Button createButton(String label, String event) {
        return new Button(label, () -> eventManager.notify(event, null))
                .setPreferredSize(new TerminalSize(35, 3))
                .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.CENTER, GridLayout.Alignment.CENTER));
    }
}