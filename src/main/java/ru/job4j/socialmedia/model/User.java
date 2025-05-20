package ru.job4j.socialmedia.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.job4j.socialmedia.security.model.Role;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @Size(max = 20)
    @Schema(description = "Имя пользователя", example = "Сергей")
    private String name;

    @NotBlank
    @Size(max = 50)
    @Email(message = "поле email не соответсвует формату")
    @Schema(description = "Почта для регистрации", example = "email@mail.ru")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    @Length(
            min = 6,
            message = "пароль должен содержать не менее 6 символов"
    )
    @Schema(description = "Пароль для регистрации", example = "password")
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "список постов пользователя", accessMode = Schema.AccessMode.READ_ONLY)
    private List<Post> posts;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}
