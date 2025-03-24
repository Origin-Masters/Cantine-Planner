package de.htwsaar.cantineplanner.businessLogic.controller.eventdata;

public class ShowSuccessScreenData extends EventData {

    public ShowSuccessScreenData(String message) {
        super(message);
    }

    public String getMessage() {
        return (String) super.getData();
    }
}
