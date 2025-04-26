package ru.job4j.socialmedia.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "posts")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор поста", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "Заголовок не может быть пустым")
    @Schema(description = "Заголовок поста", example = "Мой первый заголовок")
    private String title;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "Контент поста", example = "Описание моего поста")
    private String content;

    @Schema(description = "Дата создание поста", example = "2023-10-15T15:15:15")
    private LocalDateTime created = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Schema(description = "Пользователь данного поста", accessMode = Schema.AccessMode.READ_ONLY)
    private User user;
}
