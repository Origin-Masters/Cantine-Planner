package de.htwsaar.cantineplanner.dataAccess;

import static org.junit.jupiter.api.Assertions.*;

import de.htwsaar.cantineplanner.businessLogic.EventManager;
import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;
import de.htwsaar.cantineplanner.codegen.tables.records.ReviewRecord;
import de.htwsaar.cantineplanner.codegen.tables.records.UsersRecord;
import de.htwsaar.cantineplanner.exceptions.MealDoesntExistException;
import de.htwsaar.cantineplanner.exceptions.UserDoesntExistException;
import de.htwsaar.cantineplanner.exceptions.UserNotValidatedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DBConnectionTest {

    private DBConnection dbConnection;


    @BeforeEach
    public void setUp() {
        EventManager eventManager = new EventManager();
        dbConnection = new DBConnection();
    }

    @Test
    public void testValidateUser() {
        assertThrows(UserNotValidatedException.class,
                () -> dbConnection.validateUser("invalidUser", "invalidPassword"));
    }

    @Test
    public void testExampleUser() {
        assertDoesNotThrow(() -> {
            boolean result = dbConnection.validateUser("test", "test");
            assertTrue(result);
        });
    }

    @Test
    public void testRegisterUser() {
        assertDoesNotThrow(() -> {
            UsersRecord user = dbConnection.registerUser("testUser", "testPassword", "test@example.com");
            assertNotNull(user);
            assertEquals("testUser", user.getUsername());
        });
    }

    @Test
    public void testGetUserId() {
        assertThrows(UserDoesntExistException.class, () -> dbConnection.getUserId("nonExistentUser"));
    }

    @Test
    public void testAddMeal() {
        MealsRecord meal = new MealsRecord();
        meal.setName("Test Meal");
        meal.setPrice(10.0F);
        meal.setCalories(500);
        meal.setAllergy("None");
        meal.setMeat(0);

        assertDoesNotThrow(() -> dbConnection.addMeal(meal));
    }

    @Test
    public void testGetAllMeals() {
        assertDoesNotThrow(() -> {
            List<MealsRecord> meals = dbConnection.getAllMeals();
            assertNotNull(meals);
        });
    }

    @Test
    public void testDeleteMeal() {
        int mealId = 1;
        assertThrows(MealDoesntExistException.class, () -> dbConnection.deleteMeal(mealId));
    }

    @Test
    public void testSearchMeal() {
        String mealName = "Test Meal";
        assertDoesNotThrow(() -> {
            List<MealsRecord> meals = dbConnection.searchMeal(mealName);
            assertNotNull(meals);
        });
    }

    @Test
    public void testMealDetails() {
        int mealId = 1;
        assertThrows(MealDoesntExistException.class, () -> dbConnection.mealDetails(mealId));
    }

    @Test
    public void testGetAllReviews() {
        assertDoesNotThrow(() -> {
            List<ReviewRecord> reviews = dbConnection.getAllReviews();
            assertNotNull(reviews);
        });
    }

    @Test
    public void testGetAllReviewsByUser() {
        int userId = 1;
        assertDoesNotThrow(() -> {
            List<ReviewRecord> reviews = dbConnection.getAllReviewsByUser(userId);
            assertNotNull(reviews);
        });
    }

    @Test
    public void testReviewsByMealId() {
        int mealId = 1;
        assertDoesNotThrow(() -> {
            List<ReviewRecord> reviews = dbConnection.reviewsByMealiD(mealId);
            assertNotNull(reviews);
        });
    }

    @Test
    public void testReviewsByMealName() {
        String mealName = "Test Meal";
        assertDoesNotThrow(() -> dbConnection.reviewsByMealName(mealName));
    }

    @Test
    public void testDeleteReview() {
        int ratingId = 1;
        assertDoesNotThrow(() -> dbConnection.deleteReview(ratingId));
    }

    @Test
    public void testAddReview() {
        ReviewRecord review = new ReviewRecord();
        review.setMealId(1);
        review.setRating(5);
        review.setComment("Great!");

        assertDoesNotThrow(() -> dbConnection.addReview(review));
    }
}