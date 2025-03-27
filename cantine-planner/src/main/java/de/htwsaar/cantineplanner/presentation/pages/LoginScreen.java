package de.htwsaar.cantineplanner.presentation.pages;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.SGR;
import de.htwsaar.cantineplanner.businessLogic.manager.EventManager;
import de.htwsaar.cantineplanner.businessLogic.types.eventdata.EventType;
import de.htwsaar.cantineplanner.businessLogic.types.eventdata.StringArrayData;

import java.util.List;

/**
 * The LoginScreen class is responsible for building and displaying a login screen
 * with username and password input fields in a terminal-based GUI.
 */
public class LoginScreen extends AbstractScreen {
    private final EventManager eventManager;

    /**
     * Constructs a LoginScreen with the specified GUI and event manager.
     *
     * @param gui the MultiWindowTextGUI instance
     * @param eventManager the EventManager instance
     */
    public LoginScreen(MultiWindowTextGUI gui, EventManager eventManager) {
        super(gui);
        this.eventManager = eventManager;
    }

    /**
     * Displays the login screen with username and password input fields,
     * and handles the login, register, and exit actions.
     */
    @Override
    public void display() {
        Panel panel = new Panel(new GridLayout(2));
        GridLayout gridLayout = (GridLayout) panel.getLayoutManager();
        gridLayout.setHorizontalSpacing(5);
        gridLayout.setVerticalSpacing(2);

        panel.addComponent(new Label("Username").setForegroundColor(new TextColor.RGB(29, 29, 29)).addStyle(SGR.BOLD));
        TextBox username = new TextBox().setSize(new TerminalSize(30, 1));
        panel.addComponent(username);

        panel.addComponent(new Label("Password").setForegroundColor(new TextColor.RGB(29, 29, 29)).addStyle(SGR.BOLD));
        TextBox password = new TextBox().setMask('*').setSize(new TerminalSize(30, 1));
        panel.addComponent(password);

        panel.addComponent(new EmptySpace());

        Panel buttonPanel = new Panel(new GridLayout(1));
        buttonPanel.addComponent(new Button("Login", () -> {
            String user = username.getText();
            String pass = password.getText();
            eventManager.notify(EventType.LOGIN, new StringArrayData(new String[]{user, pass}));
        }).setPreferredSize(new TerminalSize(20, 3)).setLayoutData(
                GridLayout.createLayoutData(GridLayout.Alignment.BEGINNING, GridLayout.Alignment.BEGINNING)));

        buttonPanel.addComponent(new Button("Register", () -> eventManager.notify(EventType.SHOW_REGISTER_SCREEN, null)).setPreferredSize(new TerminalSize(20, 3)).setLayoutData(
                GridLayout.createLayoutData(GridLayout.Alignment.BEGINNING, GridLayout.Alignment.BEGINNING)));

        buttonPanel.addComponent(new Button("Exit", () -> eventManager.notify(EventType.EXIT, null)).setPreferredSize(new TerminalSize(20, 3)).setLayoutData(
                GridLayout.createLayoutData(GridLayout.Alignment.CENTER, GridLayout.Alignment.CENTER)));

        panel.addComponent(buttonPanel,
                GridLayout.createLayoutData(GridLayout.Alignment.CENTER, GridLayout.Alignment.CENTER, true, false, 1,
                        1));

        BasicWindow window = new BasicWindow("Login");
        window.setComponent(panel);
        window.setHints(List.of(Window.Hint.CENTERED));

        gui.addWindowAndWait(window);
    }
}