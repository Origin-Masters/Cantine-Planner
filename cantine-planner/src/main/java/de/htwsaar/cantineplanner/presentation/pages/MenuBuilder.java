package de.htwsaar.cantineplanner.presentation.pages;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import de.htwsaar.cantineplanner.businessLogic.EventManager;
import de.htwsaar.cantineplanner.businessLogic.controller.eventdata.EventType;
import de.htwsaar.cantineplanner.businessLogic.controller.eventdata.IntData;

import java.util.ArrayList;
import java.util.List;
public class MenuBuilder {
    private final MultiWindowTextGUI gui;
    private final EventManager eventManager;
    private String title;
    private List<MenuButton> buttons;
    public MenuBuilder(MultiWindowTextGUI gui, EventManager eventManager) {
        this.gui = gui;
        this.eventManager = eventManager;
        this.buttons = new ArrayList<>();
    }
    public MenuBuilder setTitle(String title) {
        this.title = title;
        return this;
    }
    // Einzelnen Button hinzufügen
    public MenuBuilder addButton(String label, EventType event) {
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

        for (MenuButton button : buttons) {
            panel.addComponent(new Button(button.getLabel(), () -> eventManager.notify(button.getEvent(), button.getData()))
                    .setPreferredSize(new TerminalSize(35, 3))
                    .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.CENTER, GridLayout.Alignment.CENTER)));
        }
        BasicWindow window = new BasicWindow(title);
        window.setComponent(panel);
        window.setHints(List.of(Window.Hint.CENTERED));
        gui.addWindowAndWait(window);
    }
    // Öffentliche statische Klasse für Buttons
    public static class MenuButton {
        private final String label;
        private final EventType event;
        private IntData data;
        public MenuButton(String label, EventType event) {
            this.label = label;
            this.event = event;
        }
        public MenuButton(String label, EventType event, IntData data) {
            this.label = label;
            this.event = event;
            this.data = data;
        }
        public String getLabel() {
            return label;
        }
        public EventType getEvent() {
            return event;
        }
        public IntData getData() {
            return data;
        }
    }
}