package ru.job4j.socialmedia.validation;

import java.util.List;

public record ValidationErrorResponse(List<Violation> violations) {
}
