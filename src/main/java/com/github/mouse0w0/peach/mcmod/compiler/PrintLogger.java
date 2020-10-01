package com.github.mouse0w0.peach.mcmod.compiler;

import java.io.PrintStream;

public class PrintLogger implements Logger {
    private PrintStream out;

    public PrintLogger() {
        this(System.out);
    }

    public PrintLogger(PrintStream out) {
        this.out = out;
    }

    @Override
    public void info(String msg) {
        out.println(msg);
    }

    @Override
    public void info(String msg, Object... args) {
        out.println(String.format(msg, args));
    }

    @Override
    public void info(String msg, Throwable throwable) {
        out.println(msg);
        throwable.printStackTrace(out);
    }

    @Override
    public void warn(String msg) {
        out.println(msg);
    }

    @Override
    public void warn(String msg, Object... args) {
        out.println(String.format(msg, args));
    }

    @Override
    public void warn(String msg, Throwable throwable) {
        out.println(msg);
        throwable.printStackTrace(out);
    }

    @Override
    public void error(String msg) {
        out.println(msg);
    }

    @Override
    public void error(String msg, Object... args) {
        out.println(String.format(msg, args));
    }

    @Override
    public void error(String msg, Throwable throwable) {
        out.println(msg);
        throwable.printStackTrace(out);
    }
}
