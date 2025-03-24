package de.htwsaar.cantineplanner.presentation.pages;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.TextColor;

import java.util.Arrays;
import java.util.List;

public class NotificationScreenBuilder extends AbstractScreen {
    private final String notificationMessage;
    private final TextColor textColor;

    public NotificationScreenBuilder(MultiWindowTextGUI gui, String notificationMessage, TextColor textColor) {
        super(gui);
        this.notificationMessage = notificationMessage;
        this.textColor = textColor;
    }

    @Override
    public void display() {
        Panel panel = new Panel(new GridLayout(1));
        GridLayout gridLayout = (GridLayout) panel.getLayoutManager();
        gridLayout.setHorizontalSpacing(5);
        gridLayout.setVerticalSpacing(3);
        gridLayout.setTopMarginSize(3);

        panel.addComponent(new Label(notificationMessage)
                .setForegroundColor(textColor));

        Button closeButton = new Button("Close", () -> {
            gui.getActiveWindow().close();
        });
        panel.addComponent(closeButton);

        BasicWindow window = new BasicWindow("Notification");
        window.setComponent(panel);
        window.setHints(List.of(Window.Hint.CENTERED));

        gui.addWindowAndWait(window);
    }
}