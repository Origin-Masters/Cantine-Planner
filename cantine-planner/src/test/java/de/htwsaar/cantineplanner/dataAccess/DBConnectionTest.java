package de.htwsaar.cantineplanner.dataAccess;

import static org.junit.jupiter.api.Assertions.*;

import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;
import de.htwsaar.cantineplanner.codegen.tables.records.ReviewRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DBConnectionTest {

    private DBConnection dbConnection;


    @BeforeEach
    public void setUp() {
        dbConnection = new DBConnection();
    }

    @Test
    public void testAddMeal() {
        MealsRecord meal = new MealsRecord();
        meal.setName("Test Meal");
        meal.setPrice(10.0F);
        meal.setCalories(500);
        meal.setAllergy("None");

        assertDoesNotThrow(() -> dbConnection.addMeal(meal));
    }

    @Test
    public void testGetAllMeals() {
        assertDoesNotThrow(() -> dbConnection.getAllMeals());
    }

    @Test
    public void testDeleteMeal() {
        int mealId = 1;
        assertDoesNotThrow(() -> dbConnection.deleteMeal(mealId));
    }

    @Test
    public void testSearchMeal() {
        String mealName = "Test Meal";
        assertDoesNotThrow(() -> dbConnection.searchMeal(mealName));
    }

    @Test
    public void testMealDetails() {
        int mealId = 1;
        assertDoesNotThrow(() -> dbConnection.mealDetails(mealId));
    }

    @Test
    public void testAllReviews() {
        assertDoesNotThrow(() -> dbConnection.allReviews());
    }

    @Test
    public void testReviewByMealId() {
        int mealId = 1;
        assertDoesNotThrow(() -> dbConnection.reviewByMealiD(mealId));
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