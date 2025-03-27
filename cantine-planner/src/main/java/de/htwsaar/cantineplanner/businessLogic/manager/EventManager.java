package de.htwsaar.cantineplanner.businessLogic.manager;


import de.htwsaar.cantineplanner.businessLogic.types.eventdata.EventData;
import de.htwsaar.cantineplanner.businessLogic.types.eventdata.EventType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Einfacher EventManager, der es ermöglicht, Listener für Events zu registrieren
 * und diese bei Bedarf aufzurufen.
 */
public class EventManager {
    private final Map<EventType, List<Consumer<EventData>>> listeners = new HashMap<>();

    public void subscribe(EventType eventType, Consumer<EventData> eventFunction) {
        List<Consumer<EventData>> list = new ArrayList<>();
        if (listeners.containsKey(eventType)) {
            list = listeners.get(eventType);
        }
        list.add(eventFunction);
        listeners.put(eventType, list);

    }

    public void subscribe(EventType eventType, Runnable eventFunction) {
        subscribe(eventType, eventData -> eventFunction.run());
    }

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
