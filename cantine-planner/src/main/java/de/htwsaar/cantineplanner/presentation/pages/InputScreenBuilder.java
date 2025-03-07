package de.htwsaar.cantineplanner.presentation.pages;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import de.htwsaar.cantineplanner.businessLogic.EventManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InputScreenBuilder {
    private final MultiWindowTextGUI gui;
    private final EventManager eventManager;
    private final String title;
    private final List<TextBox> textBoxes;

    public InputScreenBuilder(MultiWindowTextGUI gui, EventManager eventManager, String title) {
        this.gui = gui;
        this.eventManager = eventManager;
        this.textBoxes = new ArrayList<>();
        this.title = title;
    }

    // Method to add a labeled TextBox to the panel
    private void addLabeledTextBox(Panel panel, String labelText) {
        panel.addComponent(new Label(labelText)
                .setForegroundColor(new TextColor.RGB(29, 29, 29))
                .addStyle(SGR.BOLD));
        TextBox textBox = new TextBox();
        textBoxes.add(textBox);
        panel.addComponent(textBox);
    }


    // Method to display the panel with a list of labels
    public void display(List<String> labels, String event) {
        Panel panel = new Panel(new GridLayout(2));
        GridLayout gridLayout = (GridLayout) panel.getLayoutManager();
        gridLayout.setHorizontalSpacing(5);
        gridLayout.setVerticalSpacing(2);

        // Add labeled TextBoxes for each label in the list
        for (String label : labels) {
            addLabeledTextBox(panel, label);
        }
        panel.addComponent(new EmptySpace(), GridLayout.createHorizontallyFilledLayoutData(2));

        Panel buttonPanel = new Panel(new GridLayout(2));
        buttonPanel.addComponent(new Button("Close", () -> {
            gui.getActiveWindow().close();
        }));
        buttonPanel.addComponent(new Button("Submit", () -> {
            List<String> values = new ArrayList<>();
            for (TextBox textBox : textBoxes) {
                values.add(textBox.getText());
            }
            eventManager.notify(event, values.toArray(new String[0]));
        }));

        panel.addComponent(buttonPanel, GridLayout.createHorizontallyFilledLayoutData(2));

        BasicWindow window = new BasicWindow(title);
        window.setComponent(panel);
        window.setHints(Arrays.asList(Window.Hint.CENTERED));

        gui.addWindowAndWait(window);
    }
}