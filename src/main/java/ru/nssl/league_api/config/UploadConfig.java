package ru.nssl.league_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class UploadConfig {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @PostConstruct
    public void init() {
        try {
            Path path = Paths.get(System.getProperty("user.dir"))
                    .resolve(uploadDir);

            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

        } catch (Exception e) {
            throw new RuntimeException("Cannot create upload directory", e);
        }
    }
}