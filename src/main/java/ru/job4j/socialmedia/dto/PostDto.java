package ru.job4j.socialmedia.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Schema(description = "DTO для поста")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    @Schema(description = "Уникальный идентификатор поста", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "Заголовок не может быть пустым")
    @Schema(description = "Заголовок поста", example = "Мой первый заголовок")
    private String title;

    @Schema(description = "Контент поста", example = "Описание моего поста")
    private String content;

    @Schema(description = "Дата создание поста", example = "2023-10-15T15:15:15")
    private LocalDateTime created = LocalDateTime.now();
}
