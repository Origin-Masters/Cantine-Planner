package de.htwsaar.cantineplanner.dataAccess;

import static org.junit.jupiter.api.Assertions.*;

import de.htwsaar.cantineplanner.businessLogic.EventManager;
import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;
import de.htwsaar.cantineplanner.codegen.tables.records.ReviewRecord;
import de.htwsaar.cantineplanner.codegen.tables.records.UsersRecord;
import de.htwsaar.cantineplanner.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

public class DBConnectionTest {

    private DBConnection dbConnection;

    String PATH_TO_TEST_PROPERTIES = "hikari-test.properties";

    @BeforeEach
    public void setUp() {
        EventManager eventManager = new EventManager();
        dbConnection = new DBConnection(PATH_TO_TEST_PROPERTIES);
    }

    @Test
    public void testValidateUser() {
        assertThrows(UserNotValidatedException.class,
                () -> dbConnection.validateUser("invalidUser", "invalidPassword"));
        assertThrows(UserNotValidatedException.class,
                () -> dbConnection.validateUser("invalidUser2", "invalidPassword2"));
    }

    @Test
    public void testExampleUser() {
        assertDoesNotThrow(() -> {
            boolean result = dbConnection.validateUser("test", "test");
            assertTrue(result);
        });
    }

    /*


    @Test
    public void testRegisterUser() {
        assertDoesNotThrow(() -> {

            UsersRecord user2 = dbConnection.registerUser("testUser2", "testPassword2", "test2@example.com");
            assertNotNull(user2);
            assertEquals("testUser2", user2.getUsername());
        });
    }

    */

    @Test
    public void testRegisterUserWithSameUsername() {
        assertThrows(UserAlreadyExistsException.class, () ->
        {
            dbConnection.registerUser("testUser", "testPassword", "test@example.com");
        });
    }
    @Test
    public void testGetUserId() {
        assertThrows(UserDoesntExistException.class, () -> dbConnection.getUserId("nonExistentUser"));
        assertThrows(UserDoesntExistException.class, () -> dbConnection.getUserId("nonExistentUser2"));
    }

    @Test
    public void testAddMealsWhichAlreadyExist() {
        MealsRecord meal1 = new MealsRecord();
        meal1.setName("Test Meal 1");
        meal1.setPrice(15.0F);
        meal1.setCalories(600);
        meal1.setAllergy("None");
        meal1.setMeat(1);

        MealsRecord meal2 = new MealsRecord();
        meal2.setName("Test Meal 2");
        meal2.setPrice(20.0F);
        meal2.setCalories(700);
        meal2.setAllergy("N");
        meal2.setMeat(0);

        assertThrows(MealAlreadyExistsException.class,() -> dbConnection.addMeal(meal1));
        assertThrows(MealAlreadyExistsException.class,() -> dbConnection.addMeal(meal2));
    }

    @Test
    public void testSearchMealWithDifferentParameters() {
        assertDoesNotThrow(() -> {
            List<MealsRecord> meals1 = dbConnection.searchMeal("Test Meal 1");
            assertNotNull(meals1);

            List<MealsRecord> meals2 = dbConnection.searchMeal("Test Meal 2");
            assertNotNull(meals2);
        });
    }

    @Test
    public void testReviewsByMealIdWithDifferentParameters() {
        assertDoesNotThrow(() -> {
            List<ReviewRecord> reviews1 = dbConnection.reviewsByMealiD(1);
            assertNotNull(reviews1);

            List<ReviewRecord> reviews2 = dbConnection.reviewsByMealiD(2);
            assertNotNull(reviews2);
        });
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
        int mealId2 = 2;
        assertThrows(MealDoesntExistException.class, () -> dbConnection.deleteMeal(mealId2));
    }

    @Test
    public void testSearchMeal() {
        String mealName = "Test Meal";
        assertDoesNotThrow(() -> {
            List<MealsRecord> meals = dbConnection.searchMeal(mealName);
            assertNotNull(meals);
        });

        String mealName2 = "Test Meal 2";
        assertDoesNotThrow(() -> {
            List<MealsRecord> meals = dbConnection.searchMeal(mealName2);
            assertNotNull(meals);
        });
    }

    @Test
    public void testMealDetails() {
        int mealId = 1;
        assertThrows(MealDoesntExistException.class, () -> dbConnection.mealDetails(mealId));
        int mealId2 = 2;
        assertThrows(MealDoesntExistException.class, () -> dbConnection.mealDetails(mealId2));
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

        int userId2 = 2;
        assertDoesNotThrow(() -> {
            List<ReviewRecord> reviews = dbConnection.getAllReviewsByUser(userId2);
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

        int mealId2 = 2;
        assertDoesNotThrow(() -> {
            List<ReviewRecord> reviews = dbConnection.reviewsByMealiD(mealId2);
            assertNotNull(reviews);
        });
    }

    @Test
    public void testReviewsByMealName() {
        String mealName = "Test Meal";
        assertDoesNotThrow(() -> dbConnection.reviewsByMealName(mealName));

        String mealName2 = "Test Meal 2";
        assertDoesNotThrow(() -> dbConnection.reviewsByMealName(mealName2));
    }

    @Test
    public void testDeleteReview() {
        int ratingId = 1;
        assertThrows(ReviewiDDoesntExistException.class,() -> dbConnection.deleteReview(ratingId));

        /*
        int ratingId2 = 2;
        assertDoesNotThrow(() -> dbConnection.deleteReview(ratingId2));

        */
    }

    @Test
    public void testAddReview() {
        ReviewRecord review = new ReviewRecord();
        review.setMealId(1);
        review.setRating(5);
        review.setComment("Great Really Good ! Now in Test DB ! !");

        ReviewRecord review2 = new ReviewRecord();
        review2.setMealId(2);
        review2.setRating(4);
        review2.setComment("Good!");

        assertDoesNotThrow(() -> dbConnection.addReview(review));
        assertDoesNotThrow(() -> dbConnection.addReview(review2));
    }
}