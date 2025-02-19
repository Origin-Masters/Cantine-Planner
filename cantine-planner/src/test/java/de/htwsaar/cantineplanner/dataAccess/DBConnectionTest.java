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
    public void testRegisterUserWithSameUsername() {
        assertDoesNotThrow(() -> {
            dbConnection.deleteUserByName("testUser"); // Delete the user if it already exists
            dbConnection.registerUser("testUser", "testPassword", "test@example.com");
        });
        assertThrows(UserAlreadyExistsException.class, () -> {
            dbConnection.registerUser("testUser", "testPassword", "test@example.com");
        });
    }

    @Test
    public void testGetUserId() {
        assertThrows(UserDoesntExistException.class, () -> dbConnection.getUserId("nonExistentUser"));
        assertThrows(UseriDDoesntExcistException.class, () -> dbConnection.getUserId("nonExistentUser2"));

        //testing for existing user
        assertDoesNotThrow(() -> {
            int userId = dbConnection.getUserId("Xudong");
            assertNotNull(userId);
        });

    }

    @Test
    public void testAddMealsWhichAlreadyExist() {
        MealsRecord meal1 = new MealsRecord();
        meal1.setName("Test Meal 1");
        meal1.setPrice(15.0F);
        meal1.setCalories(600);
        meal1.setAllergy("None");
        meal1.setMeat(1);
        meal1.setDay("Mon");

        MealsRecord meal2 = new MealsRecord();
        meal2.setName("Test Meal 2");
        meal2.setPrice(20.0F);
        meal2.setCalories(700);
        meal2.setAllergy("N");
        meal2.setMeat(0);
        meal2.setDay("Tue");

        assertThrows(MealAlreadyExistsException.class, () -> dbConnection.addMeal(meal1));
        assertThrows(MealAlreadyExistsException.class, () -> dbConnection.addMeal(meal2));
    }

    @Test
    public void testSearchMealWithDifferentParameters() {
        assertDoesNotThrow(() -> {
            List<MealsRecord> meals1 = dbConnection.searchMealByName("Test Meal 1");
            assertNotNull(meals1);

            List<MealsRecord> meals2 = dbConnection.searchMealByName("Test Meal 2");
            assertNotNull(meals2);
        });
    }

    @Test
    public void testReviewsByMealIdWithDifferentParameters() {
        assertDoesNotThrow(() -> {
            List<ReviewRecord> reviews1 = dbConnection.reviewsByMealName("Test Meal 2");
            List<ReviewRecord> reviews3 = dbConnection.reviewsByMealName("Pozole");
            assertNotNull(reviews3);
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
    public void testDeleteMealById() throws SQLException {
        MealsRecord meal3 = new MealsRecord();
        meal3.setName("Test Meal 3");
        meal3.setPrice(15.0F);
        meal3.setCalories(600);
        meal3.setAllergy("None");
        meal3.setMeat(1);

        MealsRecord meal4 = new MealsRecord();
        meal4.setName("Test Meal 4");
        meal4.setPrice(20.0F);
        meal4.setCalories(700);
        meal4.setAllergy("N");
        meal4.setMeat(0);

        assertDoesNotThrow(() -> dbConnection.addMeal(meal3));
        assertDoesNotThrow(() -> dbConnection.addMeal(meal4));

        int mealId3 = dbConnection.searchMealByName("Test Meal 3").get(0).getMealId();
        int mealId4 = dbConnection.searchMealByName("Test Meal 4").get(0).getMealId();

        assertDoesNotThrow(() -> dbConnection.deleteMealById(mealId3));
        assertDoesNotThrow(() -> dbConnection.deleteMealById(mealId4));
    }

    @Test
    public void testSearchMealByName() {
        String mealName = "Test Meal";
        assertDoesNotThrow(() -> {
            List<MealsRecord> meals = dbConnection.searchMealByName(mealName);
            assertNotNull(meals);
        });

        String mealName2 = "Test Meal 2";
        assertDoesNotThrow(() -> {
            List<MealsRecord> meals = dbConnection.searchMealByName(mealName2);
            assertNotNull(meals);
        });
    }
    
    @Test
    public void testSearchMealById() throws SQLException {
        int mealId = 1;
        assertThrows(MealiDNotFoundException.class, () -> dbConnection.searchMealById(mealId));
        int mealId2 = 2;
        assertThrows(MealiDNotFoundException.class, () -> dbConnection.searchMealById(mealId2));

        // Testing for existing meal
        assertDoesNotThrow(() -> {
            List<MealsRecord> meals = dbConnection.searchMealById(3);
            assertNotNull(meals);
        });
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
    public void testReviewsByMealName() {
        String mealName = "Test Meal";
        assertDoesNotThrow(() -> dbConnection.reviewsByMealName(mealName));

        String mealName2 = "Test Meal 2";
        assertDoesNotThrow(() -> dbConnection.reviewsByMealName(mealName2));
    }

    @Test
    public void testDeleteReview() throws SQLException {
        int nonExistentRatingId = 1000; // Assuming 1000 is a non-existent rating ID
        assertThrows(ReviewiDDoesntExistException.class, () -> dbConnection.deleteReview(nonExistentRatingId));

        // Add a review first
        ReviewRecord review = new ReviewRecord();
        review.setMealId(1);
        review.setRating(5);
        review.setComment("Test review");
        review.setUserid(1);

        assertDoesNotThrow(() -> dbConnection.addReview(review));

        // Get the ratingId of the added review
        int ratingId = dbConnection.getAllReviews().get(0).getRatingId();

        // Now delete the review
        assertDoesNotThrow(() -> dbConnection.deleteReview(ratingId));
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

    @Test
    public void testIsAdmin() {
        assertThrows(UserDoesntExistException.class,
                () -> dbConnection.isAdmin(9999)); // Assuming 9999 is a non-existent user ID

        assertDoesNotThrow(() -> {
            boolean isAdmin = dbConnection.isAdmin(9);
            assertFalse(isAdmin);
        });

        assertDoesNotThrow(() -> {
            boolean isAdmin = dbConnection.isAdmin(16);
            assertFalse(isAdmin);
        });
    }

    @Test
    void deleteUserById() {
        // Add a user first
        assertDoesNotThrow(() -> {
            UsersRecord user1 = dbConnection.registerUser("testUserToDelete1", "testPassword1",
                    "testToDelete1@example.com");
            assertNotNull(user1);
            int userId1 = user1.getUserid();

            UsersRecord user2 = dbConnection.registerUser("testUserToDelete2", "testPassword2",
                    "testToDelete2@example.com");
            assertNotNull(user2);
            int userId2 = user2.getUserid();

            // Now delete the user
            assertDoesNotThrow(() -> dbConnection.deleteUserById(userId1));
            assertDoesNotThrow(() -> dbConnection.deleteUserById(userId2));
        });
    }

    @Test
    void deleteUserByName() {
        // Add a user first
        assertDoesNotThrow(() -> {
            UsersRecord user1 = dbConnection.registerUser("testUserToDeleteByName1", "testPassword1",
                    "testToDeleteByName1@example.com");
            assertNotNull(user1);   // Check if user is added successfully

            UsersRecord user2 = dbConnection.registerUser("testUserToDeleteByName2", "testPassword2",
                    "testToDeleteByName2@example.com");
            assertNotNull(user2);   // Check if user is added successfully

            // Now delete the user
            assertDoesNotThrow(() -> dbConnection.deleteUserByName("testUserToDeleteByName1"));
            assertDoesNotThrow(() -> dbConnection.deleteUserByName("testUserToDeleteByName2"));
        });
    }

    @Test
    void getWeeklyPlan() {
    }

    @Test
    void editWeeklyPlan() {
    }

    @Test
    void resetWeeklyPlan() {
    }

    @Test
    void editUserData() {
    }

    @Test
    void isValidEmail() {
    }

    @Test
    void isAdmin() {
    }
}

