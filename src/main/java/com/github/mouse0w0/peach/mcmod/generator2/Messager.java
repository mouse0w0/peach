package com.github.mouse0w0.peach.mcmod.generator2;

public interface Messager {
    int getWarnCount();

    int getErrorCount();

    void log(Message message);

    void log(Message message, Object... args);
}
