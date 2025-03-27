package de.htwsaar.cantineplanner.data.exceptions;

public class InvalidEmailTypeException extends RuntimeException {
  public InvalidEmailTypeException(String message) {
    super(message);
  }
}
