package ru.job4j.socialmedia.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Schema(description = "DTO для пользователя")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @Schema(description = "Уникальный идентификатор пользователя", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "поле name не может быть пустым")
    @Schema(description = "Имя пользователя", example = "Сергей")
    private String name;

    @Email(message = "поле email не соответсвует формату")
    @Schema(description = "Почта для регистрации", example = "email@mail.ru")
    private String email;

    @NotBlank
    @Length(
            min = 4,
            max = 10,
            message = "пароль должен быть от 3 до 10 символов"
    )
    @Schema(description = "Пароль для регистрации", example = "password")
    private String password;
}
