package de.htwsaar.cantineplanner.businessLogic.controller.eventdata;

public class StringArrayData extends EventData{

    public StringArrayData(String[] message) {
        super(message);
    }

    public String[] getMessage() {
        return (String[]) super.getData();
    }
}
