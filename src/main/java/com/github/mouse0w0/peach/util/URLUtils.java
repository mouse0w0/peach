package com.github.mouse0w0.peach.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class URLUtils {
    public static BufferedReader newBufferedReader(URL url) throws IOException {
        return newBufferedReader(url, StandardCharsets.UTF_8);
    }

    public static BufferedReader newBufferedReader(URL url, Charset charset) throws IOException {
        return new BufferedReader(new InputStreamReader(url.openStream(), charset.newDecoder()));
    }

    private URLUtils() {
    }
}
