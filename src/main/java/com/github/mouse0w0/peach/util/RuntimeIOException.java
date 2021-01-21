package com.github.mouse0w0.peach.util;

import java.io.IOException;

public class RuntimeIOException extends RuntimeException {

    public RuntimeIOException(IOException e) {
        super(e.getMessage(), e);
    }
}
