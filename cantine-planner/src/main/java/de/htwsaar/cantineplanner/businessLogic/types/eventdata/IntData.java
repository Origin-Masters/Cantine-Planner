package de.htwsaar.cantineplanner.businessLogic.types.eventdata;

public class IntData extends EventData{

    public IntData(int data) {
        super(data);
    }

    public int getMessage() {
        return (int) super.getData();
    }

}
