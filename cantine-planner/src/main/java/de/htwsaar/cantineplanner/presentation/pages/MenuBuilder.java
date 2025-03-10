package de.htwsaar.cantineplanner.presentation.pages;
import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import de.htwsaar.cantineplanner.businessLogic.EventManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class MenuBuilder {
    private MultiWindowTextGUI gui;
    private EventManager eventManager;
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

        for (MenuButton button : buttons) {
            panel.addComponent(new Button(button.getLabel(), () -> eventManager.notify(button.getEvent(), null))
                    .setPreferredSize(new TerminalSize(35, 3))
                    .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.CENTER, GridLayout.Alignment.CENTER)));
        }
        BasicWindow window = new BasicWindow(title);
        window.setComponent(panel);
        window.setHints(Arrays.asList(Window.Hint.CENTERED));
        gui.addWindowAndWait(window);
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
}