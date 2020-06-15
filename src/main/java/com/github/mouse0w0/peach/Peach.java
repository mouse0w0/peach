package com.github.mouse0w0.peach;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.i18n.Translator;
import com.github.mouse0w0.i18n.source.ClasspathFolderTranslationSource;
import com.github.mouse0w0.peach.ui.FXApplication;
import javafx.application.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public class Peach {

    public static final Logger LOGGER = LoggerFactory.getLogger(Peach.class);

    public static void main(String[] args) {
        LOGGER.info("Locale: {}", Locale.getDefault());
        I18n.setTranslator(Translator.builder()
                .locale(Locale.getDefault())
                .source(new ClasspathFolderTranslationSource("lang"))
                .build());
        Application.launch(FXApplication.class, args);
    }
}
