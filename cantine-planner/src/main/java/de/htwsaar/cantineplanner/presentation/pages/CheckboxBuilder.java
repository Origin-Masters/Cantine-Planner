package de.htwsaar.cantineplanner.presentation.pages;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import de.htwsaar.cantineplanner.businessLogic.EventManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckboxBuilder extends AbstractScreen {
    private final EventManager eventManager;
    private String title;
    private List<CheckBox> checkBoxes;

    public CheckboxBuilder(MultiWindowTextGUI gui, EventManager eventManager) {
        super(gui);
        this.eventManager = eventManager;
        this.checkBoxes = new ArrayList<>();
    }

    public CheckboxBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public CheckboxBuilder addCheckbox(String label) {
        this.checkBoxes.add(new CheckBox(label));
        return this;
    }

    @Override
    public void display() {
        Panel panel = new Panel(new LinearLayout(Direction.VERTICAL));
        panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        panel.addComponent(new EmptySpace(new TerminalSize(1, 1))); // Spacing

        Label titleLabel = new Label(title)
                .setForegroundColor(TextColor.ANSI.CYAN_BRIGHT)
                .addStyle(SGR.BOLD);
        panel.addComponent(titleLabel);

        panel.addComponent(new EmptySpace(new TerminalSize(1, 1))); // More Spacing
        panel.addComponent(new Separator(Direction.HORIZONTAL));

        for (CheckBox checkBox : checkBoxes) {
            panel.addComponent(checkBox);
        }

        panel.addComponent(new Separator(Direction.HORIZONTAL));

        Panel buttonPanel = getPanel();
        panel.addComponent(buttonPanel);

        panel.addComponent(new EmptySpace(new TerminalSize(1, 1))); // Final Spacing

        BasicWindow window = new BasicWindow(title);
        window.setComponent(panel.withBorder(Borders.doubleLine(" Options ")));
        window.setHints(Arrays.asList(Window.Hint.CENTERED));

        gui.addWindowAndWait(window);
    }

    private Panel getPanel() {
        Panel buttonPanel = new Panel(new GridLayout(2));
        Button submitButton = new Button("Submit", () -> {
            List<String> selected = new ArrayList<>();
            for (CheckBox checkBox : checkBoxes) {
                if (checkBox.isChecked()) {
                    selected.add(checkBox.getLabel());
                }
            }
            eventManager.notify("checkbox_submit", selected);
        });

        Button closeButton = new Button("Close", () -> {
            if (gui.getActiveWindow() != null) {
                gui.getActiveWindow().close();
            }
        });

        buttonPanel.addComponent(submitButton);
        buttonPanel.addComponent(closeButton);
        return buttonPanel;
    }
}
