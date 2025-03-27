package de.htwsaar.cantineplanner.presentation.pages;

        import com.googlecode.lanterna.TerminalSize;
        import com.googlecode.lanterna.gui2.*;
        import de.htwsaar.cantineplanner.businessLogic.manager.EventManager;
        import de.htwsaar.cantineplanner.businessLogic.types.eventdata.EventType;
        import de.htwsaar.cantineplanner.businessLogic.types.eventdata.IntData;

        import java.util.ArrayList;
        import java.util.List;

        /**
         * The MenuBuilder class is responsible for building and displaying a menu
         * with buttons in a terminal-based GUI.
         */
        public class MenuBuilder {
            private final MultiWindowTextGUI gui;
            private final EventManager eventManager;
            private String title;
            private List<MenuButton> buttons;

            /**
             * Constructs a MenuBuilder with the specified GUI and event manager.
             *
             * @param gui the MultiWindowTextGUI instance
             * @param eventManager the EventManager instance
             */
            public MenuBuilder(MultiWindowTextGUI gui, EventManager eventManager) {
                this.gui = gui;
                this.eventManager = eventManager;
                this.buttons = new ArrayList<>();
            }

            /**
             * Sets the title of the menu.
             *
             * @param title the title of the menu
             * @return the current instance of MenuBuilder
             */
            public MenuBuilder setTitle(String title) {
                this.title = title;
                return this;
            }

            /**
             * Adds a button to the menu with the specified label and event.
             *
             * @param label the label of the button
             * @param event the event associated with the button
             * @return the current instance of MenuBuilder
             */
            public MenuBuilder addButton(String label, EventType event) {
                this.buttons.add(new MenuButton(label, event));
                return this;
            }

            /**
             * Sets the list of buttons for the menu.
             *
             * @param buttons the list of MenuButton instances
             * @return the current instance of MenuBuilder
             */
            public MenuBuilder setButtons(List<MenuButton> buttons) {
                this.buttons = buttons;
                return this;
            }

            /**
             * Displays the menu in the GUI.
             */
            public void display() {
                Panel panel = new Panel(new GridLayout(1));
                GridLayout gridLayout = (GridLayout) panel.getLayoutManager();
                gridLayout.setHorizontalSpacing(3);
                gridLayout.setVerticalSpacing(2);

                for (MenuButton button : buttons) {
                    panel.addComponent(new Button(button.getLabel(), () -> eventManager.notify(button.getEvent(), button.getData()))
                            .setPreferredSize(new TerminalSize(35, 3))
                            .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.CENTER, GridLayout.Alignment.CENTER)));
                }
                BasicWindow window = new BasicWindow(title);
                window.setComponent(panel);
                window.setHints(List.of(Window.Hint.CENTERED));
                gui.addWindowAndWait(window);
            }

            /**
             * The MenuButton class represents a button in the menu with a label,
             * an associated event, and optional data.
             */
            public static class MenuButton {
                private final String label;
                private final EventType event;
                private IntData data;

                /**
                 * Constructs a MenuButton with the specified label and event.
                 *
                 * @param label the label of the button
                 * @param event the event associated with the button
                 */
                public MenuButton(String label, EventType event) {
                    this.label = label;
                    this.event = event;
                }

                /**
                 * Constructs a MenuButton with the specified label, event, and data.
                 *
                 * @param label the label of the button
                 * @param event the event associated with the button
                 * @param data the data associated with the event
                 */
                public MenuButton(String label, EventType event, IntData data) {
                    this.label = label;
                    this.event = event;
                    this.data = data;
                }

                /**
                 * Gets the label of the button.
                 *
                 * @return the label of the button
                 */
                public String getLabel() {
                    return label;
                }

                /**
                 * Gets the event associated with the button.
                 *
                 * @return the event associated with the button
                 */
                public EventType getEvent() {
                    return event;
                }

                /**
                 * Gets the data associated with the event.
                 *
                 * @return the data associated with the event
                 */
                public IntData getData() {
                    return data;
                }
            }
        }