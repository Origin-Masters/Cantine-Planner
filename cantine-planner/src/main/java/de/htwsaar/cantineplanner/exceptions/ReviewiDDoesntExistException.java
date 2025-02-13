package de.htwsaar.cantineplanner.exceptions;

public class ReviewiDDoesntExistException extends RuntimeException {
    public ReviewiDDoesntExistException(String message) {
        super(message);
    }
}
