package de.htwsaar.cantineplanner.data;

import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;
import de.htwsaar.cantineplanner.dataAccess.HikariCPDataSource;
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
        assertDoesNotThrow(() -> {
            List<MealsRecord> meals = mealsRepository.sortMealsByPrice();
            assertNotNull(meals);
            assertFalse(meals.isEmpty());
            for (int i = 1; i < meals.size(); i++) {
                assertTrue(meals.get(i - 1).getPrice() <= meals.get(i).getPrice());
            }
        });

    }

    @Test
    void sortMealsByRating() {
        assertDoesNotThrow(() -> {
            List<MealsRecord> meals = mealsRepository.sortMealsByRating();
            assertNotNull(meals);
            assertFalse(meals.isEmpty());
            for (int i = 1; i < meals.size(); i++) {
                double rating1 = mealsRepository.calculateMedianRatingForMeal(meals.get(i - 1).getMealId());
                double rating2 = mealsRepository.calculateMedianRatingForMeal(meals.get(i).getMealId());
                assertTrue(rating1 >= rating2);
            }
        });
    }

    @Test
    void sortMealsByName() {
        assertDoesNotThrow(() -> {
            List<MealsRecord> meals = mealsRepository.sortMealsByName();
            assertNotNull(meals);
            assertFalse(meals.isEmpty());
            for (int i = 1; i < meals.size(); i++) {
                assertTrue(meals.get(i - 1).getName().compareTo(meals.get(i).getName()) <= 0);
            }
        });
    }

    @Test
    void sortMealsByCalories() {
        assertDoesNotThrow(() -> {
            List<MealsRecord> meals = mealsRepository.sortMealsByCalories();
            assertNotNull(meals);
            assertFalse(meals.isEmpty());
            for (int i = 1; i < meals.size(); i++) {
                assertTrue(meals.get(i - 1).getCalories() <= meals.get(i).getCalories());
            }
        });
    }

    @Test
    void sortMealsByAllergy() {
        assertDoesNotThrow(() -> {
            List<MealsRecord> meals = mealsRepository.sortMealsByAllergy(16);
            assertNotNull(meals);
            assertFalse(meals.isEmpty());
            for (MealsRecord meal : meals) {
                String allergy = meal.getAllergy();
                if (allergy != null && !allergy.isEmpty()) {
                    String[] mealAllergies = allergy.split(",");
                    for (String allergyItem : mealAllergies) {
                        assertFalse(allergyItem.trim().equals("G") || allergyItem.trim().equals("T"));
                    }
                }
            }
        });
    }


    @Test
    void getAllAllergies() {
        assertDoesNotThrow(() -> {
            List<MealsRecord> allergies = mealsRepository.getAllAllergies();
            assertNotNull(allergies);
            assertFalse(allergies.isEmpty());
        });
    }

    @Test
    void addMeal() {
        MealsRecord meal1 = new MealsRecord();
        meal1.setName("Test Meal 1");
        meal1.setPrice(15.0F);
        meal1.setCalories(600);
        meal1.setAllergy("None");
        meal1.setMeat(1);
        meal1.setDay("Wed");

        MealsRecord meal2 = new MealsRecord();
        meal2.setName("Test Meal 2");
        meal2.setPrice(20.0F);
        meal2.setCalories(100);
        meal2.setAllergy("N");
        meal2.setMeat(0);
        meal2.setDay("Fri");

        assertThrows(MealAlreadyExistsException.class, () -> mealsRepository.addMeal(meal1));
        assertThrows(MealAlreadyExistsException.class, () -> mealsRepository.addMeal(meal2));
    }

    @Test
    void editMeal() {
        assertDoesNotThrow(() -> {
            MealsRecord meal = mealsRepository.searchMealByName("Test Meal 1").get(0);
            String originalName = meal.getName();
            float originalPrice = meal.getPrice();
            int originalCalories = meal.getCalories();
            String originalAllergy = meal.getAllergy();
            int originalMeat = meal.getMeat();

            meal.setName("Updated Meal");
            meal.setPrice(12.5F);
            meal.setCalories(500);
            meal.setAllergy("None");
            meal.setMeat(1);

            mealsRepository.editMeal(meal);

            MealsRecord updatedMeal = mealsRepository.searchMealByName("Updated Meal").get(0);
            assertEquals("Updated Meal", updatedMeal.getName());
            assertEquals(12.5F, updatedMeal.getPrice());
            assertEquals(500, updatedMeal.getCalories());
            assertEquals("None", updatedMeal.getAllergy());
            assertEquals(1, updatedMeal.getMeat());

            // Restore original values
            meal.setName(originalName);
            meal.setPrice(originalPrice);
            meal.setCalories(originalCalories);
            meal.setAllergy(originalAllergy);
            meal.setMeat(originalMeat);
            mealsRepository.editMeal(meal);
        });
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