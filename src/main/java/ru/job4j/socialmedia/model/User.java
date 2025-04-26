package ru.job4j.socialmedia.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Schema(description = "User Model Information")
@Data
@Entity
@Table(name = "users")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор пользователя", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "поле name не может быть пустым")
    @Schema(description = "Имя пользователя", example = "Сергей")
    private String name;

    @Email(message = "поле email не соответсвует формату")
    @Schema(description = "Почта для регистрации", example = "email@mail.ru")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    @Length(
            min = 4,
            max = 10,
            message = "пароль должен быть от 3 до 10 символов"
    )
    @Schema(description = "Пароль для регистрации", example = "password")
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "список постов пользователя", accessMode = Schema.AccessMode.READ_ONLY)
    private List<Post> posts;
}
