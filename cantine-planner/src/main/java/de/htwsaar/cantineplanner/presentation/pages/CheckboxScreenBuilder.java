package de.htwsaar.cantineplanner.presentation.pages;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import de.htwsaar.cantineplanner.businessLogic.EventManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckboxScreenBuilder {
    private MultiWindowTextGUI gui;
    private EventManager eventManager;
    private String title;
    private List<CheckBox> checkBoxes;

    public CheckboxScreenBuilder(MultiWindowTextGUI gui, EventManager eventManager, String title) {
        this.gui = gui;
        this.eventManager = eventManager;
        this.checkBoxes = new ArrayList<>();
        this.title = title;
    }

    // Methode zum Hinzufügen einer Checkbox mit Beschriftung
    private void addLabeledCheckBox(Panel panel, String labelText) {
        CheckBox checkBox = new CheckBox(labelText);
        checkBoxes.add(checkBox);
        panel.addComponent(checkBox);
    }

    // Methode zur Anzeige des Panels mit einer Liste von Labels
    public void display(List<String> labels, String event) {
        Panel panel = new Panel(new GridLayout(1));
        GridLayout gridLayout = (GridLayout) panel.getLayoutManager();
        gridLayout.setHorizontalSpacing(5);
        gridLayout.setVerticalSpacing(2);

        panel.addComponent(new Label(title)
                .setForegroundColor(TextColor.ANSI.BLACK_BRIGHT)
                .addStyle(SGR.BOLD));

        // Labeled CheckBoxes hinzufügen
        for (String label : labels) {
            addLabeledCheckBox(panel, label);
        }
        panel.addComponent(new EmptySpace());

        Panel buttonPanel = new Panel(new GridLayout(2));
        buttonPanel.addComponent(new Button("Close", () -> gui.getActiveWindow().close()));
        buttonPanel.addComponent(new Button("Submit", () -> {
            List<String> selectedValues = new ArrayList<>();
            for (CheckBox checkBox : checkBoxes) {
                if (checkBox.isChecked()) {
                    selectedValues.add(checkBox.getLabel());
                }
            }
            String result = String.join(",", selectedValues);
            eventManager.notify(event, new String[]{result});
        }));

        panel.addComponent(buttonPanel);

        BasicWindow window = new BasicWindow(title);
        window.setComponent(panel);
        window.setHints(Arrays.asList(Window.Hint.CENTERED));

        gui.addWindowAndWait(window);
    }
}
