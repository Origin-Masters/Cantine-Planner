package de.htwsaar.cantineplanner.presentation.pages;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import de.htwsaar.cantineplanner.businessLogic.manager.EventManager;
import de.htwsaar.cantineplanner.businessLogic.types.eventdata.EventType;
import de.htwsaar.cantineplanner.businessLogic.types.eventdata.StringArrayData;

import java.util.ArrayList;
import java.util.List;

/**
 * The InputScreenBuilder class is responsible for building and displaying a screen
 * with input text boxes in a terminal-based GUI.
 */
public class InputScreenBuilder {
    private final MultiWindowTextGUI gui;
    private final EventManager eventManager;
    private final String title;
    private final List<TextBox> textBoxes;

    /**
     * Constructs an InputScreenBuilder with the specified GUI, event manager, and title.
     *
     * @param gui the MultiWindowTextGUI instance
     * @param eventManager the EventManager instance
     * @param title the title of the screen
     */
    public InputScreenBuilder(MultiWindowTextGUI gui, EventManager eventManager, String title) {
        this.gui = gui;
        this.eventManager = eventManager;
        this.textBoxes = new ArrayList<>();
        this.title = title;
    }

    /**
     * Adds a labeled TextBox to the specified panel.
     *
     * @param panel the panel to which the TextBox will be added
     * @param labelText the label text of the TextBox
     */
    private void addLabeledTextBox(Panel panel, String labelText) {
        panel.addComponent(new Label(labelText)
                .setForegroundColor(new TextColor.RGB(29, 29, 29))
                .addStyle(SGR.BOLD));
        TextBox textBox = new TextBox();
        textBoxes.add(textBox);
        panel.addComponent(textBox);
    }

    /**
     * Displays the panel with a list of labels and handles the submit and close actions.
     *
     * @param labels the list of labels for the TextBoxes
     * @param eventType the event type to be notified on submit
     */
    public void display(List<String> labels, EventType eventType) {
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
        buttonPanel.addComponent(new Button("Close", () -> gui.getActiveWindow().close()));
        buttonPanel.addComponent(new Button("Submit", () -> {
            ArrayList<String> values = new ArrayList<>();
            for (TextBox textBox : textBoxes) {
                values.add(textBox.getText());
            }
            eventManager.notify(eventType, new StringArrayData(values.toArray(new String[0])));
        }));

        panel.addComponent(buttonPanel, GridLayout.createHorizontallyFilledLayoutData(2));

        BasicWindow window = new BasicWindow(title);
        window.setComponent(panel);
        window.setHints(List.of(Window.Hint.CENTERED));

        gui.addWindowAndWait(window);
    }
}