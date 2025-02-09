package de.htwsaar.cantineplanner.presentation.pages;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import de.htwsaar.cantineplanner.businessLogic.EventManager;
import de.htwsaar.cantineplanner.presentation.ScreenManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class  MenuBuilder {
    private MultiWindowTextGUI gui;
    private EventManager eventManager;
   private ScreenManager screenManager; // Add this field
    private String title;
    private List<MenuButton> buttons;

    public MenuBuilder(MultiWindowTextGUI gui, EventManager eventManager, ScreenManager screenManager) {
        this.gui = gui;
        this.eventManager = eventManager;
       this.screenManager = screenManager;
        this.buttons = new ArrayList<>();
    }

    public MenuBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    // Einzelnen Button hinzufügen
    public MenuBuilder addButton(String label, String event) {
        this.buttons.add(new MenuButton(label, event));
        return this;
    }

    // Liste von Buttons hinzufügen
    public MenuBuilder setButtons(List<MenuButton> buttons) {
        this.buttons = buttons;
        return this;
    }

    // Menü anzeigen
    public void display() {
        Panel panel = new Panel(new GridLayout(1));
        GridLayout gridLayout = (GridLayout) panel.getLayoutManager();
        gridLayout.setHorizontalSpacing(3);
        gridLayout.setVerticalSpacing(2);

        panel.addComponent(new Label(title)
                .setForegroundColor(TextColor.ANSI.BLACK_BRIGHT)
                .addStyle(SGR.BOLD));

        for (MenuButton button : buttons) {
            panel.addComponent(new Button(button.getLabel(), () -> {
                debugButtonClick(button.getLabel(), button.getEvent());// Debugging
                switch (button.getEvent()) {
                    case "addMeal":
                        screenManager.showAddMealScreen();
                        break;
                    case "deleteMeal":
                        screenManager.showInputScreen("Delete Meal", "deleteMeal", Arrays.asList("Meal ID"));
                        break;
                    case "showMealDetailsById":
                        screenManager.showInputScreen("Enter Meal ID", "showMealDetailsById", Arrays.asList("Meal ID"));
                        break;
                    case "searchMealByName":
                        screenManager.showInputScreen("Enter Meal Name", "searchMealByName", Arrays.asList("Meal Name"));
                        break;
                    default:
                        eventManager.notify(button.getEvent(), null);
                        break;
                }
            })
                    .setPreferredSize(new TerminalSize(35, 3))
                    .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.CENTER, GridLayout.Alignment.CENTER)));
        }
        BasicWindow window = new BasicWindow(title);
        window.setComponent(panel);
        window.setHints(Arrays.asList(Window.Hint.CENTERED));

        gui.addWindowAndWait(window);
        try {
            gui.updateScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
}
    // Öffentliche statische Klasse für Buttons
    public static class MenuButton {
        private String label;
        private String event;

        public MenuButton(String label, String event) {
            this.label = label;
            this.event = event;
        }

        public String getLabel() {
            return label;
        }

        public String getEvent() {
            return event;
        }
    }

    private void debugButtonClick(String buttonLabel, String event) {
        System.out.println("Button clicked: " + buttonLabel);
        System.out.println("Event triggered: " + event);
    }
}
