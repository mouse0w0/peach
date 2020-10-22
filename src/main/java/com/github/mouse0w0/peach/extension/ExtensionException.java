package com.github.mouse0w0.peach.extension;

public class ExtensionException extends RuntimeException {
    public ExtensionException(String message) {
        super(message);
    }

    public ExtensionException(String message, Throwable cause) {
        super(message, cause);
    }
}
