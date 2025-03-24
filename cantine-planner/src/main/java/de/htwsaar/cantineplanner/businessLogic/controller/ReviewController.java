package de.htwsaar.cantineplanner.businessLogic.controller;

import de.htwsaar.cantineplanner.businessLogic.CantineService;
import de.htwsaar.cantineplanner.businessLogic.EventManager;
import de.htwsaar.cantineplanner.codegen.tables.records.ReviewRecord;
import de.htwsaar.cantineplanner.codegen.tables.records.UsersRecord;
import de.htwsaar.cantineplanner.exceptions.MealDoesntExistException;
import de.htwsaar.cantineplanner.exceptions.ReviewiDDoesntExistException;
import de.htwsaar.cantineplanner.presentation.ScreenManager;

import java.sql.SQLException;
import java.util.List;

public class ReviewController extends AbstractController {

    private final int currentUserId;
    public ReviewController(ScreenManager screenManager,
                            CantineService cantineService,
                            EventManager eventManager,
                            UsersRecord userRecord) {
        super(screenManager, cantineService, eventManager, userRecord);
        this.subscribeToEvents();

        currentUserId = userRecord.getUserid();
    }

    @Override
    protected void subscribeToEvents() {

        // Review-bezogene Events
        eventManager.subscribe("showAddReview", (data) -> screenManager.showAddReviewScreen());
        eventManager.subscribe("addReview", this::handleAddReview);
        eventManager.subscribe("showDeleteReview", (data) -> screenManager.showDeleteReviewScreen());
        eventManager.subscribe("deleteReview", this::handleDeleteReview);
        eventManager.subscribe("showAllReviews", this::handleShowAllReviews);
        eventManager.subscribe("showSearchReviewsByMealName", (data) -> screenManager.showSearchReviewsByMealName());
        eventManager.subscribe("searchReviewsByMealName", this::handleSearchReviewsByMealName);


    }

    public void showReviewMenu() {
        screenManager.showReviewsMenu();
    }

    /**
     * Handles adding a review for a meal.
     * <p>
     * Parses review data, creates a new review record, and adds it.
     * </p>
     *
     * @param data an Object array containing rating, comment, and meal ID as Strings
     */
    public void handleAddReview(Object data) {
        try {
            String[] reviewData = (String[]) data;
            int mealId = Integer.parseInt(reviewData[2]);
            int rating = Integer.parseInt(reviewData[0]);
            String comment = reviewData[1];

            if (rating < 0 || rating > 5) {
                screenManager.showErrorScreen("Rating must be between 0 and 5.");
                return;
            }

            ReviewRecord review = new ReviewRecord();
            review.setMealId(mealId);
            review.setRating(rating);
            review.setComment(comment);
            review.setUserid(currentUserId);
            cantineService.addReview(review);
            screenManager.closeActiveWindow();
            screenManager.showSuccessScreen("Review added successfully!");
        } catch (NumberFormatException e) {
            screenManager.showErrorScreen("Invalid input format!");
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while adding review please try again!");
        }
    }


    /**
     * Handles deleting a review.
     * <p>
     * Parses the review ID from input data and deletes the corresponding review.
     * </p>
     *
     * @param data an Object array where the first element is the review ID as a String
     */
    private void handleDeleteReview(Object data) {
        try {
            String[] reviewData = (String[]) data;
            int reviewId = Integer.parseInt(reviewData[0]);
            int reviewUserId = cantineService.getUserIdFromReviewId(reviewId);
            // Check if the current user is admin
            boolean isAdmin = cantineService.isAdmin(currentUserId);
            // If not admin and review's user id does not match, reject deletion
            if (!isAdmin && reviewUserId != currentUserId) {
                screenManager.showErrorScreen("Unauthorized: You can only delete your own reviews.");
                return;
            }

            // Authorized to delete review
            cantineService.deleteReview(reviewId);
            screenManager.showSuccessScreen("Review deleted successfully!");
        } catch (NumberFormatException e) {
            screenManager.showErrorScreen("Invalid review ID format!");
        } catch (ReviewiDDoesntExistException e) {
            screenManager.showErrorScreen(e.getMessage());
        } catch ( SQLException e) {
            screenManager.showErrorScreen("Error deleting review, please try again!");
        }
    }

    /**
     * Handles displaying all reviews.
     * <p>
     * Retrieves a list of all reviews and displays them.
     * </p>
     *
     * @param data not used
     */
    private void handleShowAllReviews(Object data) {
        try {
            List<ReviewRecord> reviews = cantineService.getAllReviews();
            screenManager.showAllReviews(reviews);
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while showing all Reviews please try again!");
        }
    }

    /**
     * Handles searching reviews by meal name.
     * <p>
     * Retrieves reviews corresponding to the given meal name and displays them.
     * </p>
     *
     * @param data an Object array where the first element is the meal name as a String
     */
    private void handleSearchReviewsByMealName(Object data) {
        try {
            String[] dataArray = (String[]) data;
            String mealName = dataArray[0];
            List<ReviewRecord> reviews = cantineService.searchReviewsByMealName(mealName);
            screenManager.showAllReviews(reviews);
        } catch (MealDoesntExistException e) {
            screenManager.showErrorScreen(e.getMessage());
        } catch (SQLException e) {
            screenManager.showErrorScreen("There was an error while searching for the Reviews please try again!");
        }
    }









}
