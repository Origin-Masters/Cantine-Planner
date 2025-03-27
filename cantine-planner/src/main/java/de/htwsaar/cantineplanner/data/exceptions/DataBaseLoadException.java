package de.htwsaar.cantineplanner.data.exceptions;

public class DataBaseLoadException extends RuntimeException {

    public DataBaseLoadException(String message) {
        super(message);
    }

    public DataBaseLoadException(String message, Throwable cause) {
        super(message, cause);
    }

}
