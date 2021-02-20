package com.github.mouse0w0.peach.mcmod.ui;

import com.github.mouse0w0.peach.mcmod.Localizable;
import javafx.util.StringConverter;

public class LocalizableConverter<T extends Localizable> extends StringConverter<T> {

    private static final LocalizableConverter<?> INSTANCE = new LocalizableConverter<>();

    @SuppressWarnings("unchecked")
    public static <T extends Localizable> LocalizableConverter<T> instance() {
        return (LocalizableConverter<T>) INSTANCE;
    }

    @Override
    public String toString(T object) {
        return object.getLocalizedText();
    }

    @Override
    public T fromString(String string) {
        throw new UnsupportedOperationException();
    }
}
