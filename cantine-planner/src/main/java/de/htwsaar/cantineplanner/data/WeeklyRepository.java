package de.htwsaar.cantineplanner.data;

import de.htwsaar.cantineplanner.codegen.tables.Meals;
import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;
import de.htwsaar.cantineplanner.dataAccess.HikariCPDataSource;
import de.htwsaar.cantineplanner.exceptions.MealDoesntExistException;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * The WeeklyRepository class is responsible for handling all database operations related to the weekly plan.
 */
public class WeeklyRepository extends AbstractRepository {
    /**
     * Constructs a new WeeklyRepository object.
     *
     * @param dataSource an instance of HikariCPDataSource, offering a connection pool
     * for efficient and reliable database connectivity.
     */
    protected WeeklyRepository(HikariCPDataSource dataSource) {
        super(dataSource);
    }

    /**
     * Retrieves the weekly plan of meals from the database.
     * <p>
     * This method fetches all meal records from the database where the day is not null.
     * </p>
     *
     * @return a list of MealsRecord representing the weekly plan
     * @throws SQLException if a database access error occurs
     */
    protected List<MealsRecord> getWeeklyPlan() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            return dsl.selectFrom(Meals.MEALS).where(Meals.MEALS.DAY.isNotNull()).fetchInto(MealsRecord.class);
        }
    }

    /**
     * Edits the weekly plan by updating the day for a specific meal.
     * <p>
     * This method updates the day field of a meal record in the database based on the provided meal name.
     * If the meal does not exist, a MealDoesntExistException is thrown.
     * </p>
     *
     * @param mealName the name of the meal to be updated
     * @param day      the new day to be set for the meal
     * @throws SQLException             if a database access error occurs
     * @throws MealDoesntExistException if the meal with the given name does not exist
     */
    protected void editWeeklyPlan(String mealName, String day) throws SQLException, MealDoesntExistException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext create = DSL.using(connection, SQLDialect.SQLITE);
            int rowsAffected = create.update(DSL.table("meals")).set(DSL.field("day"), day).where(
                    DSL.field("Name").eq(mealName)).execute();
            if (rowsAffected == 0) {
                throw new MealDoesntExistException("Meal does not exist");
            }
        } catch (SQLException e) {
            throw new SQLException("Error updating weekly plan", e);
        }
    }

    /**
     * Resets the weekly plan.
     * <p>
     * This method sets the day field of all meal records in the database to null, effectively resetting the weekly plan.
     * </p>
     *
     * @throws SQLException if a database access error occurs
     */
    protected void resetWeeklyPlan() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            dsl.update(DSL.table("meals")).set(DSL.field("day"), (String) null).execute();
        }
    }
}
