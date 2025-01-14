package de.htwsaar.cantineplanner.presentation;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;

import java.io.IOException;

public class TUI {
    private ProgrammHelper programmHelper;
    public TUI() {
        programmHelper = new ProgrammHelper();
    }

    public int test() {
        System.out.println("----Test Menü----");
        System.out.println("1. Alle Gerichte anzeigen");
        System.out.println("2. Gericht hinzufügen");
        return programmHelper.promptNumber("> ");
    }
}