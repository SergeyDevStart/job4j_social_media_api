package ru.job4j.socialmedia.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.job4j.socialmedia.model.Post;

import java.util.List;

@Schema(description = "DTO для пользователя с его постами")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserWithPostsDto {
    @Schema(description = "Уникальный идентификатор пользователя", example = "1")
    private Long id;
    @Schema(description = "Имя пользователя", example = "Сергей")
    private String name;
    @Schema(description = "Электронная почта пользователя", example = "email@mail.ru")
    private String email;
    @Schema(description = "Список постов пользоватля")
    private List<PostDto> posts;
}
