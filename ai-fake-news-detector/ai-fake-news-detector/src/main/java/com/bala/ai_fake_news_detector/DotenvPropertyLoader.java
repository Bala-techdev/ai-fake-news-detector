package com.bala.ai_fake_news_detector;

import io.github.cdimascio.dotenv.Dotenv;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public final class DotenvPropertyLoader {

    private static final List<String> SUPPORTED_KEYS = List.of(
            "GEMINI_API_KEY",
            "DB_URL",
            "DB_USERNAME",
            "DB_PASSWORD"
    );

    private DotenvPropertyLoader() {
    }

    public static void load() {
        Path dotenvDirectory = findDotenvDirectory();
        if (dotenvDirectory == null) {
            return;
        }

        Dotenv dotenv = Dotenv.configure()
                .directory(dotenvDirectory.toAbsolutePath().toString())
                .ignoreIfMissing()
                .load();

        for (String key : SUPPORTED_KEYS) {
            if (isBlank(System.getProperty(key)) && isBlank(System.getenv(key))) {
                String value = dotenv.get(key);
                if (!isBlank(value)) {
                    System.setProperty(key, value);
                }
            }
        }
    }

    private static Path findDotenvDirectory() {
        Path current = Paths.get("").toAbsolutePath();
        while (current != null) {
            if (Files.exists(current.resolve(".env"))) {
                return current;
            }
            current = current.getParent();
        }
        return null;
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}