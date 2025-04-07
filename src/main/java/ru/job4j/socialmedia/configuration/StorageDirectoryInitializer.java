package ru.job4j.socialmedia.configuration;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class StorageDirectoryInitializer {
    @Value("${file.directory}")
    private String storageDirectory;

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Path.of(storageDirectory));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create storage directory: ", e);
        }
    }
}
