// src/main/java/de/htwsaar/cantineplanner/businessLogic/UserAlreadyExistsException.java
package de.htwsaar.cantineplanner.data.exceptions;

public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}