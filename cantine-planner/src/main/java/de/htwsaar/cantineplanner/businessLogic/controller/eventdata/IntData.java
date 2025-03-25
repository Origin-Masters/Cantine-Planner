package de.htwsaar.cantineplanner.businessLogic.controller.eventdata;

public class IntData extends EventData{

    public IntData(int data) {
        super(data);
    }

    public int getMessage() {
        return (int) super.getData();
    }

}
