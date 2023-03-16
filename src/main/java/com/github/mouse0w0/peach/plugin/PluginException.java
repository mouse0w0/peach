package com.github.mouse0w0.peach.plugin;

public class PluginException extends RuntimeException {
    public PluginException(String message) {
        super(message);
    }

    public PluginException(String message, Throwable cause) {
        super(message, cause);
    }
}
