// src/test/java/de/htwsaar/cantineplanner/businessLogic/ControllerTest.java
package de.htwsaar.cantineplanner.businessLogic;

import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;
import de.htwsaar.cantineplanner.codegen.tables.records.ReviewRecord;
import de.htwsaar.cantineplanner.exceptions.MealAlreadyExistsException;
import de.htwsaar.cantineplanner.exceptions.UserAlreadyExistsException;
import de.htwsaar.cantineplanner.exceptions.UserNotValidatedException;
import de.htwsaar.cantineplanner.presentation.ScreenManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class ControllerTest {

    private Controller controller;
    private ScreenManager screenManager;
    private CantineService cantineService;
    private EventManager eventManager;

    @BeforeEach
    public void setUp() {
        screenManager = mock(ScreenManager.class);
        cantineService = mock(CantineService.class);
        eventManager = new EventManager();
        controller = new Controller();
    }

    @Test
    public void testHandleLogin() throws SQLException, UserNotValidatedException, UserAlreadyExistsException {
        when(cantineService.validateUser("testUser", "testPassword")).thenReturn(true);
        when(cantineService.getUserId("testUser")).thenReturn(1);

        controller.handleLogin(new String[]{"testUser", "testPassword"});

        verify(screenManager).showSuccessScreen("Login successful!");
    }

    @Test
    public void testHandleRegister() throws SQLException, UserAlreadyExistsException {
        when(cantineService.registerUser("testUser", "testPassword", "test@example.com")).thenReturn(true);

        controller.handleRegister(new String[]{"testUser", "testPassword", "test@example.com"});

        verify(screenManager).showSuccessScreen("Registration successful!");
    }

    @Test
    public void testHandleAddMeal() throws SQLException, MealAlreadyExistsException {
        MealsRecord meal = new MealsRecord();
        meal.setName("Test Meal");
        meal.setPrice(10.0F);
        meal.setCalories(500);
        meal.setAllergy("None");
        meal.setMeat(0);

        controller.handleAddMeal(new String[]{"Test Meal", "10.0", "500", "None", "0"});

        verify(screenManager).showSuccessScreen("Meal added successfully!");
    }

    @Test
    public void testHandleAddReview() throws SQLException {
        ReviewRecord review = new ReviewRecord();
        review.setMealId(1);
        review.setRating(5);
        review.setComment("Great!");

        controller.handleAddReview(new String[]{"5", "Great!", "1"});

        verify(screenManager).showSuccessScreen("Review added successfully!");
    }
}