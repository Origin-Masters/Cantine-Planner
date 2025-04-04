package de.htwsaar.cantineplanner.businessLogic.types.eventdata;

public abstract class EventData {
    private final Object data;

    public EventData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }
}
