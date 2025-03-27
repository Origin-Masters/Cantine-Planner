package de.htwsaar.cantineplanner.businessLogic.controller;

import de.htwsaar.cantineplanner.businessLogic.manager.SessionManager;
import de.htwsaar.cantineplanner.businessLogic.service.CantineService;
import de.htwsaar.cantineplanner.businessLogic.manager.EventManager;
import de.htwsaar.cantineplanner.businessLogic.types.eventdata.EventData;
import de.htwsaar.cantineplanner.businessLogic.types.eventdata.EventType;
import de.htwsaar.cantineplanner.codegen.tables.records.ReviewRecord;
import de.htwsaar.cantineplanner.data.exceptions.MealDoesntExistException;
import de.htwsaar.cantineplanner.data.exceptions.ReviewiDDoesntExistException;
import de.htwsaar.cantineplanner.presentation.ScreenManager;

import java.sql.SQLException;
import java.util.List;

/**
 * The ReviewController class is responsible for handling reviews.
 * <p>
 * The ReviewController class is responsible for handling reviews, including adding a review, deleting a review, showing all reviews,
 * and searching reviews by meal name.
 * </p>
 */
public class ReviewController extends AbstractController {
    /**
     * Constructs a new ReviewController.
     * <p>
     * This constructor initializes the ReviewController with the provided ScreenManager, CantineService, and EventManager.
     * It also subscribes to the relevant events.
     * </p>
     *
     * @param screenManager  the screen manager to manage UI screens
     * @param cantineService the service to handle cantine-related operations
     * @param eventManager   the event manager to handle events
     * @param sessionManager the session manager to manage user sessions
     */
    protected ReviewController(ScreenManager screenManager,
                               CantineService cantineService,
                               EventManager eventManager,
                               SessionManager sessionManager) {
        super(screenManager, cantineService, eventManager, sessionManager);
        this.subscribeToEvents();
    }

    /**
     * Subscribes to various review-related event types and associates them with their respective handlers.
     * <p>
     * This method sets up the event subscriptions for handling different review-related events such as adding a review,
     * deleting a review, showing all reviews, and searching reviews by meal name.
     * </p>
     */
    @Override
    protected void subscribeToEvents() {
        // Review-bezogene Events
        eventManager.subscribe(EventType.SHOW_ADD_REVIEW, (data) -> screenManager.showAddReviewScreen());
        eventManager.subscribe(EventType.ADD_REVIEW, this::handleAddReview);
        eventManager.subscribe(EventType.SHOW_DELETE_REVIEW, (data) -> screenManager.showDeleteReviewScreen());
        eventManager.subscribe(EventType.DELETE_REVIEW, this::handleDeleteReview);
        eventManager.subscribe(EventType.SHOW_ALL_REVIEWS, this::handleShowAllReviews);
        eventManager.subscribe(EventType.SHOW_SEARCH_REVIEWS_BY_MEAL_NAME, (data) -> screenManager.showSearchReviewsByMealName());
        eventManager.subscribe(EventType.SEARCH_REVIEWS_BY_MEAL_NAME, this::handleSearchReviewsByMealName);
    }

    /**
     * Displays the review menu screen.
     * <p>
     * This method shows the review menu screen using the screen manager.
     * </p>
     */
    protected void showReviewMenu() {
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
    private void handleAddReview(EventData data) {
        try {
            String[] reviewData = (String[]) data.getData();
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
            review.setUserid(sessionManager.getCurrentUserId());
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
    private void handleDeleteReview(EventData data) {
        try {
            String[] reviewData = (String[]) data.getData();
            int reviewId = Integer.parseInt(reviewData[0]);
            int reviewUserId = cantineService.getUserIdFromReviewId(reviewId);
            // Check if the current user is admin
            boolean isAdmin = cantineService.isAdmin(sessionManager.getCurrentUserId());
            // If not admin and review's user id does not match, reject deletion
            if (!isAdmin && reviewUserId != sessionManager.getCurrentUserId()) {
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
        } catch (SQLException | NullPointerException  e) {
            screenManager.showErrorScreen("Error deleting review, please try again!");
        }
    }

    /**
     * Handles displaying all reviews.
     * <p>
     * Retrieves a list of all reviews and displays them.
     * </p>
     */
    private void handleShowAllReviews() {
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
    private void handleSearchReviewsByMealName(EventData data) {
        try {
            String[] dataArray = (String[]) data.getData();
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
