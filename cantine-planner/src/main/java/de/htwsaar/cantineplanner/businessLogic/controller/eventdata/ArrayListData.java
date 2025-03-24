package de.htwsaar.cantineplanner.businessLogic.controller.eventdata;

import java.util.ArrayList;

public class ArrayListData extends EventData{

    public ArrayListData(ArrayList<String> data) {
        super(data);
    }

    @Override
    public ArrayList<String> getData() {
        return (ArrayList<String>) super.getData();
    }

}
