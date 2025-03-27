package de.htwsaar.cantineplanner.data.exceptions;

public class MealDoesntExistException extends RuntimeException {
    public MealDoesntExistException(String message) {
        super(message);
    }
}
