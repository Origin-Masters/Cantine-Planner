package de.htwsaar.cantineplanner.businessLogic.controller.eventdata;

public class ShowErrorScreenData extends EventData {

    public ShowErrorScreenData(String message) {
        super(message);
    }

    public String getMessage() {
        return (String) super.getData();
    }
}
