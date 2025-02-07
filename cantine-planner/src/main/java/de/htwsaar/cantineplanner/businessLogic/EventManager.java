package de.htwsaar.cantineplanner.businessLogic;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class EventManager {
    private Map<String, Consumer<Object>> listeners = new HashMap<>();

    public void subscribe(String eventType, Consumer<Object> listener) {
        listeners.put(eventType, listener);
    }

    public void notify(String eventType, Object data) {
        if (listeners.containsKey(eventType)) {
            listeners.get(eventType).accept(data);
        }
    }


}