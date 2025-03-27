package de.htwsaar.cantineplanner.data.exceptions;

public class MealAlreadyExistsException extends RuntimeException {
    public MealAlreadyExistsException(String message) {
        super(message);
    }
}
