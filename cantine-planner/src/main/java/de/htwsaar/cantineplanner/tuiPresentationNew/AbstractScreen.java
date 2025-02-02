package de.htwsaar.cantineplanner.tuiPresentationNew;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;

public abstract class AbstractScreen {
    protected final MultiWindowTextGUI gui;

    public AbstractScreen(MultiWindowTextGUI gui) {
        this.gui = gui;
    }

    // Jeder Screen implementiert, wie er dargestellt wird
    public abstract void display();
}
