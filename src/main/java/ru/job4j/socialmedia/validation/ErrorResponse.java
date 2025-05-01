package ru.job4j.socialmedia.validation;

import java.time.LocalDateTime;

public record ErrorResponse(
        int httpStatus,
        String message,
        String type,
        String path,
        LocalDateTime timestamp
) { }
