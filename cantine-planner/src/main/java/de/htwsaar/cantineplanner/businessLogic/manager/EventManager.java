package de.htwsaar.cantineplanner.businessLogic.manager;

    import de.htwsaar.cantineplanner.businessLogic.types.eventdata.EventData;
    import de.htwsaar.cantineplanner.businessLogic.types.eventdata.EventType;

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;
    import java.util.function.Consumer;

    /**
     * Simple EventManager that allows registering listeners for events
     * and invoking them when needed.
     */
    public class EventManager {
        private final Map<EventType, List<Consumer<EventData>>> listeners = new HashMap<>();

        /**
         * Subscribes a listener to a specific event type.
         *
         * @param eventType the type of event to listen for
         * @param eventFunction the function to execute when the event occurs
         */
        public void subscribe(EventType eventType, Consumer<EventData> eventFunction) {
            List<Consumer<EventData>> list = new ArrayList<>();
            if (listeners.containsKey(eventType)) {
                list = listeners.get(eventType);
            }
            list.add(eventFunction);
            listeners.put(eventType, list);
        }

        /**
         * Subscribes a runnable to a specific event type.
         *
         * @param eventType the type of event to listen for
         * @param eventFunction the runnable to execute when the event occurs
         */
        public void subscribe(EventType eventType, Runnable eventFunction) {
            subscribe(eventType, eventData -> eventFunction.run());
        }

        /**
         * Notifies all listeners of a specific event type with the given data.
         *
         * @param eventType the type of event to notify
         * @param data the data associated with the event
         */
        public void notify(EventType eventType, EventData data) {
            if(eventType.verifyEventData(data)) {
                List<Consumer<EventData>> list = listeners.get(eventType);

                if (list != null) {
                    for (Consumer<EventData> eventFunction : list) {
                        eventFunction.accept(data);
                    }
                }
            }
        }
    }