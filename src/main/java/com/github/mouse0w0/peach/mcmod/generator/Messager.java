package com.github.mouse0w0.peach.mcmod.generator;

public abstract class Messager {

    public enum Level {
        INFO,
        WARN,
        ERROR
    }

    public abstract int getErrorCount();

    public abstract int getWarnCount();

    public abstract void log(Level level, String msg, Throwable throwable);

    public void log(Level level, String msg) {
        log(level, msg, (Throwable) null);
    }

    public void log(Level level, String msg, Object... args) {
        log(level, String.format(msg, args), (Throwable) null);
    }

    public void info(String msg) {
        log(Level.INFO, msg);
    }

    public void info(String msg, Object... args) {
        log(Level.INFO, msg, args);
    }

    public void info(String msg, Throwable throwable) {
        log(Level.INFO, msg, throwable);
    }

    public void warn(String msg) {
        log(Level.WARN, msg);
    }

    public void warn(String msg, Object... args) {
        log(Level.WARN, msg, args);
    }

    public void warn(String msg, Throwable throwable) {
        log(Level.WARN, msg, throwable);
    }

    public void error(String msg) {
        log(Level.ERROR, msg);
    }

    public void error(String msg, Object... args) {
        log(Level.ERROR, msg, args);
    }

    public void error(String msg, Throwable throwable) {
        log(Level.ERROR, msg, throwable);
    }
}
