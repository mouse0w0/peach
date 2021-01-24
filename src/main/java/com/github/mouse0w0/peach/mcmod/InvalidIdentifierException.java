package com.github.mouse0w0.peach.mcmod;

public class InvalidIdentifierException extends RuntimeException {
    public InvalidIdentifierException(String message) {
        super(message);
    }

    public InvalidIdentifierException(String message, Throwable cause) {
        super(message, cause);
    }
}
