package de.htwsaar.cantineplanner.data;

import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;
import de.htwsaar.cantineplanner.dataAccess.HikariCPDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WeeklyRepositoryTest {

    final String PATH_TO_TEST_PROPERTIES = "hikari-test.properties";
    WeeklyRepository weeklyRepository;

    @BeforeEach
    public void setUp() {

        HikariCPDataSource dataSource = new HikariCPDataSource(PATH_TO_TEST_PROPERTIES);
        weeklyRepository = new WeeklyRepository(dataSource);

    }

    @Test
    void testGetWeeklyPlan() throws SQLException {


        MealsRepository mealsRepository = new MealsRepository(new HikariCPDataSource(PATH_TO_TEST_PROPERTIES));

        MealsRecord meal1 = new MealsRecord();
        meal1.setName("Test Meal for Weekly Plan 1");
        meal1.setPrice(15.0F);
        meal1.setCalories(600);
        meal1.setAllergy("None");
        meal1.setMeat(1);
        meal1.setDay("Mon");

        assertDoesNotThrow(() -> mealsRepository.addMeal(meal1));

        assertDoesNotThrow(() -> {
            List<MealsRecord> weeklyPlan = weeklyRepository.getWeeklyPlan();
            assertNotNull(weeklyPlan);
            assertFalse(weeklyPlan.isEmpty(), "Weekly plan should not be empty");

            // Check if each meal has a name and a day
            for (MealsRecord meal : weeklyPlan) {
                assertNotNull(meal.getName(), "Meal name should not be null");
                assertNotNull(meal.getDay(), "Meal day should not be null");
            }
        });

        int mealId = mealsRepository.searchMealByName("Test Meal for Weekly Plan 1").get(0).getMealId();
        assertDoesNotThrow(() -> mealsRepository.deleteMealById(mealId));
    }

    @Test
    void testEditWeeklyPlan() {
        String mealName = "Test Meal 1";
        String day = "Mon";
        assertDoesNotThrow(() -> weeklyRepository.editWeeklyPlan(mealName, day));
    }

    @Test
    void testResetWeeklyPlan() {
        assertDoesNotThrow(() -> weeklyRepository.resetWeeklyPlan());
    }
}