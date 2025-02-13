package de.htwsaar.cantineplanner.exceptions;

public class UserNotValidatedException extends RuntimeException {
    public UserNotValidatedException(String message) {
        super(message);
    }
}
