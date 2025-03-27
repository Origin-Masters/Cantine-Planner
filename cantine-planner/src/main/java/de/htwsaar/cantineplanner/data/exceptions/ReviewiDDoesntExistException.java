package de.htwsaar.cantineplanner.data.exceptions;

public class ReviewiDDoesntExistException extends RuntimeException {
    public ReviewiDDoesntExistException(String message) {
        super(message);
    }
}
