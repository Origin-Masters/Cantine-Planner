package de.htwsaar.cantineplanner.presentation.pages;

        import com.googlecode.lanterna.gui2.*;
        import com.googlecode.lanterna.gui2.table.Table;
        import java.util.ArrayList;
        import java.util.List;

        /**
         * The TableBuilder class is responsible for building and displaying a table
         * in a terminal-based GUI.
         */
        public class TableBuilder {
            private final MultiWindowTextGUI gui;
            private final String title;
            private final List<String> columns;
            private final List<List<String>> rows;

            /**
             * Constructs a TableBuilder with the specified GUI and title.
             *
             * @param gui the MultiWindowTextGUI instance
             * @param title the title of the table
             */
            public TableBuilder(MultiWindowTextGUI gui, String title) {
                this.gui = gui;
                this.title = title;
                this.columns = new ArrayList<>();
                this.rows = new ArrayList<>();
            }

            /**
             * Adds a column to the table.
             *
             * @param column the name of the column
             * @return the current instance of TableBuilder
             */
            public TableBuilder addColumn(String column) {
                columns.add(column);
                return this;
            }

            /**
             * Adds a row to the table.
             *
             * @param row the list of values for the row
             * @return the current instance of TableBuilder
             */
            public TableBuilder addRow(List<String> row) {
                rows.add(row);
                return this;
            }

            /**
             * Displays the table in the GUI.
             */
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