package com.github.mouse0w0.peach.mcmod.compiler;

public interface Logger {

    void info(String msg);

    void info(String msg, Object... args);

    void info(String msg, Throwable throwable);

    void warn(String msg);

    void warn(String msg, Object... args);

    void warn(String msg, Throwable throwable);

    void error(String msg);

    void error(String msg, Object... args);

    void error(String msg, Throwable throwable);
}
