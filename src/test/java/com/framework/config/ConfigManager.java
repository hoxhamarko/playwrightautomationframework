package com.framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

/**
 * Lookup order: -D system property, then environment variable
 * (e.g. db.password -> DB_PASSWORD), then config/<env>.properties.
 */
public final class ConfigManager {

    private static final Properties PROPS = new Properties();

    static {
        String env = System.getProperty("env", "qa");
        String path = "/config/" + env + ".properties";
        try (InputStream in = ConfigManager.class.getResourceAsStream(path)) {
            if (in == null) {
                throw new IllegalStateException("Missing config file: " + path);
            }
            PROPS.load(in);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private ConfigManager() {
    }

    public static String get(String key) {
        String value = System.getProperty(key);
        if (value == null) {
            value = System.getenv(key.replace('.', '_').toUpperCase());
        }
        if (value == null) {
            value = PROPS.getProperty(key);
        }
        return value;
    }

    public static String require(String key) {
        String value = get(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing config value: " + key);
        }
        return value;
    }

    public static boolean headless() {
        return Boolean.parseBoolean(System.getProperty("headless", "true"));
    }
}
