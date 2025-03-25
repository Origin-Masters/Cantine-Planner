package de.htwsaar.cantineplanner.presentation.pages;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.table.Table;
import java.util.ArrayList;
import java.util.List;

public class

TableBuilder {
    private final MultiWindowTextGUI gui;
    private final String title;
    private final List<String> columns;
    private final List<List<String>> rows;

    public TableBuilder(MultiWindowTextGUI gui, String title) {
        this.gui = gui;
        this.title = title;
        this.columns = new ArrayList<>();
        this.rows = new ArrayList<>();
    }

    public TableBuilder addColumn(String column) {
        columns.add(column);
        return this;
    }

    public TableBuilder addRow(List<String> row) {
        rows.add(row);
        return this;
    }

    public void display() {
        Panel panel = new Panel(new GridLayout(1));
        Table<String> table = new Table<>(columns.toArray(new String[0]));
        table.setVisibleRows(20);

        for (List<String> row : rows) {
            table.getTableModel().addRow(row.toArray(new String[0]));
        }
        panel.addComponent(table);

        Button closeButton = new Button("Close", () -> {
            if (gui.getActiveWindow() != null) {
                gui.getActiveWindow().close();
            }
        }).setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.CENTER, GridLayout.Alignment.CENTER));
        panel.addComponent(closeButton);

        BasicWindow window = new BasicWindow(title);
        window.setComponent(panel);
        window.setHints(List.of(Window.Hint.CENTERED));

        gui.addWindowAndWait(window);
    }
}
