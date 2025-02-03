// src/main/java/de/htwsaar/cantineplanner/presentation/pages/ErrorScreen.java
package de.htwsaar.cantineplanner.presentation.pages;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.SGR;

import java.util.Arrays;

public class ErrorScreen extends AbstractScreen {
    private String errorMessage;

    public ErrorScreen(MultiWindowTextGUI gui, String errorMessage) {
        super(gui);
        this.errorMessage = errorMessage;
    }

    @Override
    public void display() {
        Panel panel = new Panel(new GridLayout(1));
        GridLayout gridLayout = (GridLayout) panel.getLayoutManager();
        gridLayout.setHorizontalSpacing(5);
        gridLayout.setVerticalSpacing(3);
        gridLayout.setTopMarginSize(3);

        panel.addComponent(new Label(errorMessage)
                .setForegroundColor(new TextColor.RGB(255, 0, 0)));

        Button closeButton = new Button("Close", () -> {
            gui.getActiveWindow().close();
        });
        panel.addComponent(closeButton);

        BasicWindow window = new BasicWindow("Error");
        window.setComponent(panel);
        window.setHints(Arrays.asList(Window.Hint.CENTERED));

        gui.addWindowAndWait(window);
    }
}