package com.github.mouse0w0.peach.util;

public class ThrowableUtils {
    public static void rethrow(Throwable t) {
        if (t != null) {
            rethrow0(t);
        }
    }

    private static <T extends Throwable> void rethrow0(Throwable t) throws T {
        throw (T) t;
    }
}
