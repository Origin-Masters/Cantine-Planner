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

public class ReviewRepository extends AbstractRepository{

    protected ReviewRepository(HikariCPDataSource dataSource) {
        super(dataSource);
    }

    /**
     * Method getAllReviews displays all reviews in the database
     */
    public List<ReviewRecord> getAllReviews() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            return dsl.selectFrom(Review.REVIEW).fetchInto(ReviewRecord.class);
        }
    }


    /**
     * Method
     *
     * @param reviewId
     * @return
     * @throws SQLException
     * @throws ReviewiDDoesntExistException
     */
    public int getUserIdFromReviewId(int reviewId) throws SQLException, ReviewiDDoesntExistException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            if (!dsl.fetchExists(dsl.selectFrom(Review.REVIEW).where(Review.REVIEW.RATING_ID.eq(reviewId)))) {
                throw new ReviewiDDoesntExistException("Review iD that was provided does not exist ! ");
            }
            return dsl.select(Review.REVIEW.USERID).from(Review.REVIEW).where(
                    Review.REVIEW.RATING_ID.eq(reviewId)).fetchOne(Review.REVIEW.USERID);
        }
    }

    /**
     * Method getAllReviews displays all reviews in the database
     */
    public List<ReviewRecord> getAllReviewsByUser(int userId) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            return dsl.selectFrom(Review.REVIEW).where(Review.REVIEW.USERID.eq(userId)).fetchInto(ReviewRecord.class);
        }
    }


    /**
     * Method reviewsByMealName searches for reviews by meal name
     *
     * @param mealName of type String of the meal to be searched
     */
    public List<ReviewRecord> reviewsByMealName(String mealName) throws SQLException, MealDoesntExistException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            if (!dsl.fetchExists(dsl.selectFrom(Meals.MEALS).where(Meals.MEALS.NAME.eq(mealName)))) {
                throw new MealDoesntExistException("Meal with name " + mealName + " doesnt exist !");
            }
            return dsl.select().from(Review.REVIEW).join(Meals.MEALS).on(
                    Review.REVIEW.MEAL_ID.eq(Meals.MEALS.MEAL_ID)).where(Meals.MEALS.NAME.eq(mealName)).fetchInto(
                    ReviewRecord.class);
        }
    }


    /**
     * Method deleteReview deletes a review from the database
     *
     * @param ratingId of type int of the review to be deleted
     */
    public void deleteReview(int ratingId) throws SQLException, ReviewiDDoesntExistException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            // if hte review iD doesnt exist, we throw an exception
            if (!dsl.fetchExists(dsl.selectFrom(Review.REVIEW).where(Review.REVIEW.RATING_ID.eq(ratingId)))) {
                throw new ReviewiDDoesntExistException("Review iD that was provided  does not exist ! ");
            }
            // deletion of the corresponding review iD
            dsl.deleteFrom(Review.REVIEW).where(Review.REVIEW.RATING_ID.eq(ratingId)).execute();
        }

    }

    /**
     * Method addReview adds a review for the database
     *
     * @param givenReview of type ReviewRecord to be added
     */
    public boolean addReview(ReviewRecord givenReview) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            dsl.insertInto(Review.REVIEW).set(Review.REVIEW.MEAL_ID, givenReview.getMealId()).set(Review.REVIEW.RATING,
                    givenReview.getRating()).set(Review.REVIEW.COMMENT, givenReview.getComment()).set(
                    Review.REVIEW.USERID, givenReview.getUserid()).execute();
            return true;
        }
    }









}
