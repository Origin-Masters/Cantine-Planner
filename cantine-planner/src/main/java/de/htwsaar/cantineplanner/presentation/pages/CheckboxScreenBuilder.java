package de.htwsaar.cantineplanner.presentation.pages;

        import com.googlecode.lanterna.gui2.*;
        import de.htwsaar.cantineplanner.businessLogic.manager.EventManager;
        import de.htwsaar.cantineplanner.businessLogic.types.eventdata.EventType;
        import de.htwsaar.cantineplanner.businessLogic.types.eventdata.StringArrayData;

        import java.util.ArrayList;
        import java.util.List;

        /**
         * The CheckboxScreenBuilder class is responsible for building and displaying a screen
         * with checkboxes in a terminal-based GUI.
         */
        public class CheckboxScreenBuilder {
            private final MultiWindowTextGUI gui;
            private final EventManager eventManager;
            private final String title;
            private final List<CheckBox> checkBoxes;

            /**
             * Constructs a CheckboxScreenBuilder with the specified GUI, event manager, and title.
             *
             * @param gui the MultiWindowTextGUI instance
             * @param eventManager the EventManager instance
             * @param title the title of the screen
             */
            public CheckboxScreenBuilder(MultiWindowTextGUI gui, EventManager eventManager, String title) {
                this.gui = gui;
                this.eventManager = eventManager;
                this.checkBoxes = new ArrayList<>();
                this.title = title;
            }

            /**
             * Adds a labeled checkbox to the specified panel.
             *
             * @param panel the panel to which the checkbox will be added
             * @param labelText the label text of the checkbox
             */
            private void addLabeledCheckBox(Panel panel, String labelText) {
                CheckBox checkBox = new CheckBox(labelText);
                checkBoxes.add(checkBox);
                panel.addComponent(checkBox);
            }

            /**
             * Displays the panel with a list of labels and handles the submit and close actions.
             *
             * @param labels the list of labels for the checkboxes
             * @param eventType the event type to be notified on submit
             */
            public void display(List<String> labels, EventType eventType) {
                Panel panel = new Panel(new GridLayout(1));
                GridLayout gridLayout = (GridLayout) panel.getLayoutManager();
                gridLayout.setHorizontalSpacing(2);
                gridLayout.setVerticalSpacing(1);

                // Add labeled checkboxes
                for (String label : labels) {
                    addLabeledCheckBox(panel, label);
                }
                panel.addComponent(new EmptySpace());

                Panel buttonPanel = new Panel(new GridLayout(2));
                buttonPanel.addComponent(new Button("Close", () -> gui.getActiveWindow().close()));
                buttonPanel.addComponent(new Button("Submit", () -> {
                    List<String> selectedValues = new ArrayList<>();
                    for (CheckBox checkBox : checkBoxes) {
                        if (checkBox.isChecked()) {
                            selectedValues.add(checkBox.getLabel());
                        }
                    }
                    String result = String.join(",", selectedValues);
                    eventManager.notify(eventType, new StringArrayData(new String[]{result}));
                }));

                panel.addComponent(buttonPanel);

                BasicWindow window = new BasicWindow(title);
                window.setComponent(panel);
                window.setHints(List.of(Window.Hint.CENTERED));

                gui.addWindowAndWait(window);
            }
        }