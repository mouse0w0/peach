package com.github.mouse0w0.peach.util;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UserProperties {

    private static final Path USER_PROPERTIES_FILE = Paths.get(SystemUtils.USER_HOME, ".peach", "properties.json");
    private static final Logger LOGGER = LoggerFactory.getLogger(UserProperties.class);

    private static UserProperties instance;

    public static UserProperties getInstance() {
        return instance;
    }

    public static void init() {
        Thread shutdownHook = new Thread(UserProperties::save);
        shutdownHook.setDaemon(true);
        Runtime.getRuntime().addShutdownHook(shutdownHook);
        load();
    }

    private static void load() {
        if (Files.exists(USER_PROPERTIES_FILE)) {
            try {
                instance = JsonUtils.readJson(USER_PROPERTIES_FILE, UserProperties.class);
            } catch (IOException e) {
                LOGGER.error("Cannot load user properties!", e);
            }
        }

        if (instance == null) {
            instance = new UserProperties();
        }
    }

    private static void save() {
        try {
            JsonUtils.writeJson(USER_PROPERTIES_FILE, instance);
        } catch (IOException e) {
            LOGGER.error("Cannot save user properties!", e);
        }
    }
}
