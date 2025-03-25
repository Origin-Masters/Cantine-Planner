package de.htwsaar.cantineplanner.data;

import com.zaxxer.hikari.HikariConfig;
import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;
import de.htwsaar.cantineplanner.dataAccess.HikariCPDataSource;
import de.htwsaar.cantineplanner.dataAccess.HikariCPDataSourceTest;
import de.htwsaar.cantineplanner.exceptions.MealAlreadyExistsException;
import de.htwsaar.cantineplanner.exceptions.MealiDNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MealsRepositoryTest {

    MealsRepository mealsRepository;

    @BeforeEach
    public void setUp() {

        String PATH_TO_TEST_PROPERTIES = "hikari-test.properties";
        HikariCPDataSource dataSource = new HikariCPDataSource(PATH_TO_TEST_PROPERTIES);
        mealsRepository = new MealsRepository(dataSource);

    }

    @Test
    void sortMealsByPrice() {
    }

    @Test
    void sortMealsByRating() {
    }

    @Test
    void sortMealsByName() {
    }

    @Test
    void sortMealsByCalories() {
    }

    @Test
    void sortMealsByAllergy() {
    }

    @Test
    void editMeal() {

    }

    @Test
    void getAllAllergies() {
    }

    @Test
    void addMeal() {
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

        assertThrows(MealAlreadyExistsException.class, () -> mealsRepository.addMeal(meal1));
        assertThrows(MealAlreadyExistsException.class, () -> mealsRepository.addMeal(meal2));
    }

    @Test
    void getAllMeals() {
        assertDoesNotThrow(() -> {
            List<MealsRecord> meals = mealsRepository.getAllMeals();
            assertNotNull(meals);
        });
    }

    @Test
    void deleteMealById() throws SQLException {
        MealsRecord meal3 = new MealsRecord();
        meal3.setName("Test Meal 3");
        meal3.setPrice(15.0F);
        meal3.setCalories(600);
        meal3.setAllergy("None");
        meal3.setMeat(1);
        meal3.setDay("Mon");

        MealsRecord meal4 = new MealsRecord();
        meal4.setName("Test Meal 4");
        meal4.setPrice(20.0F);
        meal4.setCalories(700);
        meal4.setAllergy("N");
        meal4.setMeat(0);
        meal4.setDay("Tue");

        assertDoesNotThrow(() -> mealsRepository.addMeal(meal3));
        assertDoesNotThrow(() -> mealsRepository.addMeal(meal4));

        int mealId3 = mealsRepository.searchMealByName("Test Meal 3").get(0).getMealId();
        int mealId4 = mealsRepository.searchMealByName("Test Meal 4").get(0).getMealId();

        assertDoesNotThrow(() -> mealsRepository.deleteMealById(mealId3));
        assertDoesNotThrow(() -> mealsRepository.deleteMealById(mealId4));
    }

    @Test
    void searchMealByName() {
        String mealName = "Test Meal";
        assertDoesNotThrow(() -> {
            List<MealsRecord> meals = mealsRepository.searchMealByName(mealName);
            assertNotNull(meals);
        });

        String mealName2 = "Test Meal 2";
        assertDoesNotThrow(() -> {
            List<MealsRecord> meals = mealsRepository.searchMealByName(mealName2);
            assertNotNull(meals);
        });
    }

    @Test
    void searchMealById() {
        int mealId = 1;
        assertThrows(MealiDNotFoundException.class, () -> mealsRepository.searchMealById(mealId));
        int mealId2 = 2;
        assertThrows(MealiDNotFoundException.class, () -> mealsRepository.searchMealById(mealId2));

        // Testing for existing meal
        assertDoesNotThrow(() -> {
            List<MealsRecord> meals = mealsRepository.searchMealById(3);
            assertNotNull(meals);
        });
    }

    @Test
    void calculateMedianRatingForMeal() {
        int mealId = 1;
        assertDoesNotThrow(() -> {
            double medianRating = mealsRepository.calculateMedianRatingForMeal(mealId);
            assertTrue(medianRating >= 0 && medianRating <= 5);
        });

        int mealId2 = 2;
        assertDoesNotThrow(() -> {
            double medianRating = mealsRepository.calculateMedianRatingForMeal(mealId2);
            assertTrue(medianRating >= 0 && medianRating <= 5);
        });

    }
}