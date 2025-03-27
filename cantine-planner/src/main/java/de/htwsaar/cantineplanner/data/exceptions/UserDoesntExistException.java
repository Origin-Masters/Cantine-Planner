package de.htwsaar.cantineplanner.data.exceptions;

public class UserDoesntExistException extends RuntimeException {
    public UserDoesntExistException(String message) {
        super(message);
    }
}
