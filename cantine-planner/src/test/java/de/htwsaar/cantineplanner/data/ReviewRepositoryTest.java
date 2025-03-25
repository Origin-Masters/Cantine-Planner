package de.htwsaar.cantineplanner.data;

import de.htwsaar.cantineplanner.codegen.tables.records.ReviewRecord;
import de.htwsaar.cantineplanner.dataAccess.HikariCPDataSource;
import de.htwsaar.cantineplanner.exceptions.ReviewiDDoesntExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReviewRepositoryTest {

    final String PATH_TO_TEST_PROPERTIES = "hikari-test.properties";
    ReviewRepository reviewRepository;

    @BeforeEach
    public void setUp() {

        HikariCPDataSource dataSource = new HikariCPDataSource(PATH_TO_TEST_PROPERTIES);
        reviewRepository = new ReviewRepository(dataSource);

    }

    @Test
    void getAllReviews() {
        assertDoesNotThrow(() -> {
            List<ReviewRecord> reviews = reviewRepository.getAllReviews();
            assertNotNull(reviews);
        });
    }

    @Test
    void getUserIdFromReviewId() {

    }

    @Test
    void getAllReviewsByUser() {
        int userId = 1;
        assertDoesNotThrow(() -> {
            List<ReviewRecord> reviews = reviewRepository.getAllReviewsByUser(userId);
            assertNotNull(reviews);
        });

        int userId2 = 2;
        assertDoesNotThrow(() -> {
            List<ReviewRecord> reviews = reviewRepository.getAllReviewsByUser(userId2);
            assertNotNull(reviews);
        });
    }

    @Test
    void reviewsByMealName() {
        String mealName = "Test Meal";
        assertDoesNotThrow(() -> reviewRepository.reviewsByMealName(mealName));

        String mealName2 = "Test Meal 2";
        assertDoesNotThrow(() -> reviewRepository.reviewsByMealName(mealName2));
    }

    @Test
    void deleteReview() throws SQLException {
        int nonExistentRatingId = 1000; // Assuming 1000 is a non-existent rating ID
        assertThrows(ReviewiDDoesntExistException.class, () -> reviewRepository.deleteReview(nonExistentRatingId));

        // Add a review first
        ReviewRecord review = new ReviewRecord();
        review.setMealId(1);
        review.setRating(5);
        review.setComment("Test review");
        review.setUserid(1);

        assertDoesNotThrow(() -> reviewRepository.addReview(review));

        // Get the ratingId of the last added review
        List<ReviewRecord> allReviews = reviewRepository.getAllReviews();
        int ratingId = allReviews.get(allReviews.size() - 1).getRatingId();

        // Now delete the review
        assertDoesNotThrow(() -> reviewRepository.deleteReview(ratingId));
    }

    @Test
    void addReview() throws SQLException {
        ReviewRecord review = new ReviewRecord();
        review.setMealId(1);
        review.setRating(5);
        review.setComment("Great Really Good ! Now in Test DB ! !");
        review.setUserid(10);

        ReviewRecord review2 = new ReviewRecord();
        review2.setMealId(2);
        review2.setRating(4);
        review2.setComment("Good!");
        review2.setUserid(11);

        assertDoesNotThrow(() -> reviewRepository.addReview(review));
        assertDoesNotThrow(() -> reviewRepository.addReview(review2));

        // Get the ratingId of the last added review
        List<ReviewRecord> allReviews = reviewRepository.getAllReviews();
        int ratingId1 = allReviews.get(allReviews.size() - 1).getRatingId();
        int ratingId2 = allReviews.get(allReviews.size() - 2).getRatingId();

        assertDoesNotThrow(() -> reviewRepository.deleteReview(ratingId1));
        assertDoesNotThrow(() -> reviewRepository.deleteReview(ratingId2));
    }
}