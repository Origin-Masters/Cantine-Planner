package de.htwsaar.cantineplanner.businessLogic.controller.eventdata;

public class StringData extends EventData {

    public StringData(String data) {
        super(data);
    }

    public String getMessage() {
        return (String) super.getData();
    }
}
