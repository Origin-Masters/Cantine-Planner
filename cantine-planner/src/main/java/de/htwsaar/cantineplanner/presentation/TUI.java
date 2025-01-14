package de.htwsaar.cantineplanner.presentation;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import de.htwsaar.cantineplanner.businessLogic.Meal;

import java.io.IOException;

public class TUI {
    private ProgrammHelper programmHelper;
    public TUI() {
        programmHelper = new ProgrammHelper();
    }

    public Number test() {
        System.out.println("----Test Menü----");
        System.out.println("1. Alle Gerichte anzeigen");
        System.out.println("2. Gericht hinzufügen");
        return programmHelper.promptNumber("> ");
    }
    public Meal createMeal() {
        System.out.println("----Gericht erstellen----");
        String name = programmHelper.promptString("Name");
        String allergy = programmHelper.promptString("Allergie");
        double price = (double) programmHelper.promptNumber("Preis");
        int mealId = (int) programmHelper.promptNumber("MealId");
        int calories = (int) programmHelper.promptNumber("Kalorien");
        int meat = (int) programmHelper.promptNumber("Fleisch");
        return new Meal(name, allergy, price, mealId, calories, meat);
    }
}