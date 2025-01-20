package de.htwsaar.cantineplanner.businessLogic;

import de.htwsaar.cantineplanner.dataAccess.DBConnection;
import de.htwsaar.cantineplanner.presentation.TUI;

import org.junit.*;
import static org.mockito.Mockito.*;

public class ControllerTest {
    private Controller controller;
    private TUI tuiMock;
    private DBConnection dbConnectionMock;


    @Before
    public void setUP() {
        controller = new Controller();
        tuiMock = Mockite.mock(TUI.class);
        dbConnectionMock = Molkito.mock(DBConnection.class);




    }
}