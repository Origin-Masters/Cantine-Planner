package de.htwsaar.cantineplanner.presentation.pages;

        import com.googlecode.lanterna.gui2.*;
        import com.googlecode.lanterna.TextColor;

        import java.util.List;

        /**
         * The NotificationScreenBuilder class is responsible for building and displaying a notification screen
         * with a message and a close button in a terminal-based GUI.
         */
        public class NotificationScreenBuilder extends AbstractScreen {
            private final String notificationMessage;
            private final TextColor textColor;

            /**
             * Constructs a NotificationScreenBuilder with the specified GUI, notification message, and text color.
             *
             * @param gui the MultiWindowTextGUI instance
             * @param notificationMessage the notification message to be displayed
             * @param textColor the text color of the notification message
             */
            public NotificationScreenBuilder(MultiWindowTextGUI gui, String notificationMessage, TextColor textColor) {
                super(gui);
                this.notificationMessage = notificationMessage;
                this.textColor = textColor;
            }

            /**
             * Displays the notification screen with the message and a close button.
             */
            @Override
            public void display() {
                Panel panel = new Panel(new GridLayout(1));
                GridLayout gridLayout = (GridLayout) panel.getLayoutManager();
                gridLayout.setHorizontalSpacing(5);
                gridLayout.setVerticalSpacing(3);
                gridLayout.setTopMarginSize(3);

                panel.addComponent(new Label(notificationMessage)
                        .setForegroundColor(textColor));

                Button closeButton = new Button("Close", () -> gui.getActiveWindow().close());
                panel.addComponent(closeButton);

                BasicWindow window = new BasicWindow("Notification");
                window.setComponent(panel);
                window.setHints(List.of(Window.Hint.CENTERED));

                gui.addWindowAndWait(window);
            }
        }