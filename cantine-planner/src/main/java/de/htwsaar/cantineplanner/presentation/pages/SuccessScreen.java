// src/main/java/de/htwsaar/cantineplanner/presentation/pages/SuccessScreen.java
package de.htwsaar.cantineplanner.presentation.pages;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.SGR;

import java.util.Arrays;

public class SuccessScreen extends AbstractScreen {
    private String successMessage;

    public SuccessScreen(MultiWindowTextGUI gui, String successMessage) {
        super(gui);
        this.successMessage = successMessage;
    }

    @Override
    public void display() {
        Panel panel = new Panel(new GridLayout(1));
        GridLayout gridLayout = (GridLayout) panel.getLayoutManager();
        gridLayout.setHorizontalSpacing(5);
        gridLayout.setVerticalSpacing(3);
        gridLayout.setTopMarginSize(3);

        panel.addComponent(new Label(successMessage)
                .setForegroundColor(new TextColor.RGB(0, 255, 0)));

        Button closeButton = new Button("Close", () -> {
            gui.getActiveWindow().close();
        });
        panel.addComponent(closeButton);

        BasicWindow window = new BasicWindow("Success");
        window.setComponent(panel);
        window.setHints(Arrays.asList(Window.Hint.CENTERED));

        gui.addWindowAndWait(window);
    }
}