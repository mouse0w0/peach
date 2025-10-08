package com.github.mouse0w0.peach.mcmod.generator2;

public final class Message {
    private final Level level;
    private final String message;
    private final String logMessage;

    public enum Level {
        ERROR,
        WARN,
        INFO
    }

    public static Message error(String message, String logMessage) {
        return new Message(Level.ERROR, message, logMessage);
    }

    public static Message warn(String message, String logMessage) {
        return new Message(Level.WARN, message, logMessage);
    }

    public static Message info(String message, String logMessage) {
        return new Message(Level.INFO, message, logMessage);
    }

    public Message(Level level, String message, String logMessage) {
        this.level = level;
        this.message = message;
        this.logMessage = logMessage;
    }

    public Level getLevel() {
        return level;
    }

    /**
     * Returns the message shown to the user, usually localized.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the log message printed to console, usually plain ASCII.
     */
    public String getLogMessage() {
        return logMessage;
    }
}
