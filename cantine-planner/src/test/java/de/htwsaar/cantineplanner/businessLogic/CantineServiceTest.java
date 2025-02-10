// src/test/java/de/htwsaar/cantineplanner/businessLogic/CantineServiceTest.java
package de.htwsaar.cantineplanner.businessLogic;

import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;
import de.htwsaar.cantineplanner.codegen.tables.records.ReviewRecord;
import de.htwsaar.cantineplanner.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CantineServiceTest {

    private CantineService cantineService;

    @BeforeEach
    public void setUp() {
        cantineService = new CantineService();
    }

    @Test
    public void testValidateUser() {
        assertThrows(UserNotValidatedException.class, () -> cantineService.validateUser("invalidUser", "invalidPassword"));
    }

    @Test
    public void testExampleUser() {
        assertDoesNotThrow(() -> {
            boolean result = cantineService.validateUser("test", "test");
            assertTrue(result);
        });
    }

    @Test
    public void testRegisterUser() {
        assertDoesNotThrow(() -> {
            boolean result = cantineService.registerUser("testUser", "testPassword", "test@example.com");
            assertTrue(result);
        });
    }

    @Test
    public void testGetUserId() {
        assertThrows(UserDoesntExistException.class, () -> cantineService.getUserId("nonExistentUser"));
    }

    @Test
    public void testAddMeal() {
        MealsRecord meal = new MealsRecord();
        meal.setName("Test Meal");
        meal.setPrice(10.0F);
        meal.setCalories(500);
        meal.setAllergy("None");
        meal.setMeat(0);

        assertDoesNotThrow(() -> cantineService.addMeal(meal));
    }

    @Test
    public void testGetAllMeals() {
        assertDoesNotThrow(() -> {
            List<MealsRecord> meals = cantineService.getAllMeals();
            assertNotNull(meals);
        });
    }

    @Test
    public void testDeleteMeal() {
        int mealId = 1;
        assertThrows(MealDoesntExistException.class, () -> cantineService.deleteMeal(mealId));
    }

    @Test
    public void testGetMealById() {
        int mealId = 1;
        assertThrows(MealDoesntExistException.class, () -> cantineService.getMealById(mealId));
    }

    @Test
    public void testGetMealByName() {
        String mealName = "Test Meal";
        assertDoesNotThrow(() -> {
            MealsRecord meal = cantineService.getMealByName(mealName);
            assertNotNull(meal);
        });
    }

    @Test
    public void testAddReview() {
        ReviewRecord review = new ReviewRecord();
        review.setMealId(1);
        review.setRating(5);
        review.setComment("Great!");

        assertDoesNotThrow(() -> cantineService.addReview(review));
    }

    @Test
    public void testDeleteReview() {
        int reviewId = 1;
        assertThrows(ReviewiDDoesntExistException.class, () -> cantineService.deleteReview(reviewId));
    }

    @Test
    public void testGetAllReviews() {
        assertDoesNotThrow(() -> {
            List<ReviewRecord> reviews = cantineService.getAllReviews();
            assertNotNull(reviews);
        });
    }

    @Test
    public void testSearchReviewsByMealId() {
        int mealId = 1;
        assertDoesNotThrow(() -> {
            List<ReviewRecord> reviews = cantineService.searchReviewsByMealId(mealId);
            assertNotNull(reviews);
        });
    }

    @Test
    public void testGetAllReviewsByUser() {
        int userId = 1;
        assertDoesNotThrow(() -> {
            List<ReviewRecord> reviews = cantineService.getAllReviewsByUser(userId);
            assertNotNull(reviews);
        });
    }
}