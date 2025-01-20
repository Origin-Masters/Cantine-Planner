package de.htwsaar.cantineplanner.businessLogic;

import de.htwsaar.cantineplanner.dataAccess.DBConnection;
import de.htwsaar.cantineplanner.presentation.TUI;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ControllerTest {
    private Controller controller;
    private TUI tuiMock;
    private DBConnection dbConnectionMock;

    @Before
    public void setUP() {
        controller = new Controller();
        tuiMock = Mockito.mock(TUI.class);
        dbConnectionMock = Mockito.mock(DBConnection.class);
    }

    @Test
    public void testStart() {
        when(tuiMock.mainMenue()).thenReturn(3); // Simulate user choosing to exit
        controller.start();
        verify(tuiMock, times(1)).mainMenue();
    }

    @Test
    public void testMainMenu() {
        when(tuiMock.mainMenue()).thenReturn(1);
        controller.mainMenu();
        assertEquals(1, controller.getCurrentMenu());

        when(tuiMock.mainMenue()).thenReturn(2);
        controller.mainMenu();
        assertEquals(2, controller.getCurrentMenu());

        when(tuiMock.mainMenue()).thenReturn(3);
        controller.mainMenu();
        assertFalse(controller.isRunning());
    }

    @Test
    public void testMealMenue() {
        when(tuiMock.mealMenue()).thenReturn(1);
        controller.mealMenue();
        verify(dbConnectionMock, times(1)).allMeals();

        when(tuiMock.mealMenue()).thenReturn(2);
        controller.mealMenue();
        verify(dbConnectionMock, times(1)).addMeal(any());

        when(tuiMock.mealMenue()).thenReturn(3);
        controller.mealMenue();
        verify(dbConnectionMock, times(1)).allAllergies();

        when(tuiMock.mealMenue()).thenReturn(4);
        controller.mealMenue();
        verify(dbConnectionMock, times(1)).deleteMeal(anyInt());

        when(tuiMock.mealMenue()).thenReturn(5);
        controller.mealMenue();
        verify(dbConnectionMock, times(1)).searchMeal(anyString());

        when(tuiMock.mealMenue()).thenReturn(6);
        controller.mealMenue();
        verify(dbConnectionMock, times(1)).mealDetails(anyInt());
    }

    @Test
    public void testReviewMenue() {
        when(tuiMock.reviewMenue()).thenReturn(1);
        controller.reviewMenue();
        verify(dbConnectionMock, times(1)).allReviews();

        when(tuiMock.reviewMenue()).thenReturn(4);
        controller.reviewMenue();
        verify(dbConnectionMock, times(1)).reviewByMealiD(anyInt());
    }
}