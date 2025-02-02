package de.htwsaar.cantineplanner.presentation;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import de.htwsaar.cantineplanner.tuiPresentationNew.LoginScreen;

public class App {
    public static void main(String[] args) {
        try {
            Terminal terminal = new DefaultTerminalFactory().createTerminal();
            Screen screen = new TerminalScreen(terminal);
            screen.startScreen();

            WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);

            LoginScreen loginScreen = new LoginScreen((MultiWindowTextGUI) textGUI);
            loginScreen.display();

            screen.stopScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}