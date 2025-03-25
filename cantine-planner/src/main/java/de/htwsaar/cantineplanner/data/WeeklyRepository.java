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

public class WeeklyRepository extends AbstractRepository{

    protected WeeklyRepository(HikariCPDataSource dataSource) {
        super(dataSource);

    }

    /**
     * Method getWeeklyPlan returns the weekly plan of meals
     *
     * @return List of MealsRecord
     * @throws SQLException if an SQL exception occurs
     */
    public List<MealsRecord> getWeeklyPlan() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            return dsl.selectFrom(Meals.MEALS).where(Meals.MEALS.DAY.isNotNull()).fetchInto(MealsRecord.class);

        }
    }


    /**
     * Method editWeeklyPlan edits the weekly plan
     *
     * @param mealName of type String
     * @param day      of type String
     * @throws SQLException             if an SQL exception occurs
     * @throws MealDoesntExistException if the meal doesn't exist
     */
    public void editWeeklyPlan(String mealName, String day) throws SQLException, MealDoesntExistException {
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
     * Method resetWeeklyPlan resets the weekly plan
     *
     * @throws SQLException if an SQL exception occurs
     */
    public void resetWeeklyPlan() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            dsl.update(DSL.table("meals")).set(DSL.field("day"), (String) null).execute();
        }
    }

}
