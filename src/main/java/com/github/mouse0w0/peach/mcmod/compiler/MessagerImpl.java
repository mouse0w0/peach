package com.github.mouse0w0.peach.mcmod.compiler;

import java.io.PrintStream;

public class MessagerImpl extends Messager {
    private final PrintStream out;

    private int errorCount = 0;
    private int warnCount = 0;

    public MessagerImpl() {
        this(System.out);
    }

    public MessagerImpl(PrintStream out) {
        this.out = out;
    }

    @Override
    public int getErrorCount() {
        return errorCount;
    }

    @Override
    public int getWarnCount() {
        return errorCount;
    }

    @Override
    public void log(Level level, String msg, Throwable throwable) {
        switch (level) {
            case WARN:
                warnCount++;
                break;
            case ERROR:
                errorCount++;
                break;
        }
        out.println(msg);
        if (throwable != null) {
            throwable.printStackTrace(out);
        }
    }
}
