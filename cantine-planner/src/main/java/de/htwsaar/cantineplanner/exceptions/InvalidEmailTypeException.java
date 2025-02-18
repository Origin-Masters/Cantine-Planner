package de.htwsaar.cantineplanner.exceptions;

public class InvalidEmailTypeException extends RuntimeException {
  public InvalidEmailTypeException(String message) {
    super(message);
  }
}
