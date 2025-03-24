package de.htwsaar.cantineplanner.businessLogic;


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
    private final Map<String, List<Consumer<Object>>> listeners = new HashMap<>();

    public void subscribe(String eventType, Consumer<Object> listener) {
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }

    public void notify(String eventType, Object data) {
        List<Consumer<Object>> list = listeners.get(eventType);
        if (list != null) {
            for (Consumer<Object> listener : list) {
                listener.accept(data);
            }
        }
    }
}
