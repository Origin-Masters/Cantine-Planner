package de.htwsaar.cantineplanner.data;

import de.htwsaar.cantineplanner.codegen.tables.Meals;
import de.htwsaar.cantineplanner.codegen.tables.Review;
import de.htwsaar.cantineplanner.codegen.tables.records.ReviewRecord;
import de.htwsaar.cantineplanner.dataAccess.HikariCPDataSource;
import de.htwsaar.cantineplanner.exceptions.MealDoesntExistException;
import de.htwsaar.cantineplanner.exceptions.ReviewiDDoesntExistException;
import org.jooq.DSLContext;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * The ReviewRepository class is responsible for handling all database operations related to reviews.
 */
public class ReviewRepository extends AbstractRepository {
    /**
     * Constructs a new ReviewRepository object.
     *
     * @param dataSource
     */
    protected ReviewRepository(HikariCPDataSource dataSource) {
        super(dataSource);
    }

    /**
     * Retrieves all reviews from the database.
     * <p>
     * This method fetches all review records from the database and returns them as a list of ReviewRecord objects.
     * </p>
     *
     * @return a list of ReviewRecord objects representing all reviews in the database
     * @throws SQLException if a database access error occurs
     */
    protected List<ReviewRecord> getAllReviews() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            return dsl.selectFrom(Review.REVIEW).fetchInto(ReviewRecord.class);
        }
    }

    /**
     * Retrieves the user ID associated with a given review ID.
     * <p>
     * This method fetches the user ID from the database based on the provided review ID.
     * If the review ID does not exist, a ReviewiDDoesntExistException is thrown.
     * </p>
     *
     * @param reviewId the ID of the review
     * @return the user ID associated with the review
     * @throws SQLException                 if a database access error occurs
     * @throws ReviewiDDoesntExistException if the review ID does not exist
     */
    protected int getUserIdFromReviewId(int reviewId) throws SQLException, ReviewiDDoesntExistException , NullPointerException{
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            if (!dsl.fetchExists(dsl.selectFrom(Review.REVIEW).where(Review.REVIEW.RATING_ID.eq(reviewId)))) {
                throw new ReviewiDDoesntExistException("Review iD that was provided does not exist!");
            }
            return dsl.select(Review.REVIEW.USERID).from(Review.REVIEW).where(
                    Review.REVIEW.RATING_ID.eq(reviewId)).fetchOne(Review.REVIEW.USERID);
        }
    }

    /**
     * Retrieves all reviews made by a specific user.
     * <p>
     * This method fetches all review records from the database that are associated with the provided user ID.
     * </p>
     *
     * @param userId the ID of the user whose reviews are to be retrieved
     * @return a list of ReviewRecord objects representing the reviews made by the user
     * @throws SQLException if a database access error occurs
     */
    protected List<ReviewRecord> getAllReviewsByUser(int userId) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            return dsl.selectFrom(Review.REVIEW).where(Review.REVIEW.USERID.eq(userId)).fetchInto(ReviewRecord.class);
        }
    }

    /**
     * Retrieves reviews for a specific meal by its name.
     * <p>
     * This method fetches all review records from the database that are associated with the provided meal name.
     * If the meal name does not exist, a MealDoesntExistException is thrown.
     * </p>
     *
     * @param mealName the name of the meal whose reviews are to be retrieved
     * @return a list of ReviewRecord objects representing the reviews for the meal
     * @throws SQLException             if a database access error occurs
     * @throws MealDoesntExistException if the meal with the given name does not exist
     */
    protected List<ReviewRecord> reviewsByMealName(String mealName) throws SQLException, MealDoesntExistException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            if (!dsl.fetchExists(dsl.selectFrom(Meals.MEALS).where(Meals.MEALS.NAME.eq(mealName)))) {
                throw new MealDoesntExistException("Meal with name " + mealName + " doesn't exist!");
            }
            return dsl.select().from(Review.REVIEW).join(Meals.MEALS).on(
                    Review.REVIEW.MEAL_ID.eq(Meals.MEALS.MEAL_ID)).where(Meals.MEALS.NAME.eq(mealName)).fetchInto(
                    ReviewRecord.class);
        }
    }

    /**
     * Deletes a review from the database.
     * <p>
     * This method deletes a review record from the database based on the provided rating ID.
     * If the rating ID does not exist, a ReviewiDDoesntExistException is thrown.
     * </p>
     *
     * @param ratingId the ID of the review to be deleted
     * @throws SQLException                 if a database access error occurs
     * @throws ReviewiDDoesntExistException if the review ID does not exist
     */
    protected void deleteReview(int ratingId) throws SQLException, ReviewiDDoesntExistException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            // if the review ID doesn't exist, we throw an exception
            if (!dsl.fetchExists(dsl.selectFrom(Review.REVIEW).where(Review.REVIEW.RATING_ID.eq(ratingId)))) {
                throw new ReviewiDDoesntExistException("Review ID that was provided does not exist!");
            }
            // deletion of the corresponding review ID
            dsl.deleteFrom(Review.REVIEW).where(Review.REVIEW.RATING_ID.eq(ratingId)).execute();
        }
    }

    /**
     * Adds a review to the database.
     * <p>
     * This method inserts a new review record into the database based on the provided ReviewRecord object.
     * </p>
     *
     * @param givenReview the ReviewRecord object containing the review details to be added
     * @return true if the review was successfully added
     * @throws SQLException if a database access error occurs
     */
    protected boolean addReview(ReviewRecord givenReview) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            dsl.insertInto(Review.REVIEW)
                    .set(Review.REVIEW.MEAL_ID, givenReview.getMealId())
                    .set(Review.REVIEW.RATING, givenReview.getRating())
                    .set(Review.REVIEW.COMMENT, givenReview.getComment())
                    .set(Review.REVIEW.USERID, givenReview.getUserid())
                    .execute();
            return true;
        }
    }
}
