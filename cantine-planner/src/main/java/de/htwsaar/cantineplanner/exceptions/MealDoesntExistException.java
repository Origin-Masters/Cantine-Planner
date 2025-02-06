package de.htwsaar.cantineplanner.exceptions;

public class MealDoesntExistException extends RuntimeException {
    public MealDoesntExistException(String message) {
        super(message);
    }
}
