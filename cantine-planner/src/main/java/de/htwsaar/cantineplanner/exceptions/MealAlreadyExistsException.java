package de.htwsaar.cantineplanner.exceptions;

public class MealAlreadyExistsException extends RuntimeException {
    public MealAlreadyExistsException(String message) {
        super(message);
    }
}
