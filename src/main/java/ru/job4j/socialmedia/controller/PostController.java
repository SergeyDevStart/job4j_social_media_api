package ru.job4j.socialmedia.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.job4j.socialmedia.model.Post;
import ru.job4j.socialmedia.model.User;
import ru.job4j.socialmedia.service.post.PostService;

@Tag(name = "PostController", description = "PostController management APIs")
@RestController
@RequestMapping("/api/post")
@AllArgsConstructor
@Slf4j
@Validated
public class PostController {
    private final PostService postService;

    @Operation(summary = "Получить пост по ID", description = "Возвращает пост по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пост найден",
                    content = @Content(schema = @Schema(implementation = Post.class))),
            @ApiResponse(responseCode = "404", description = "Пост не найден")
    })
    @GetMapping("/{postId}")
    public ResponseEntity<Post> get(@PathVariable("postId") @Min(1) Long postId) {
        return postService.findById(postId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Создание поста", description = "Создаёт новый пост и возвращает его URL в заголовке Location")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пост успешно создан",
                    content = @Content(schema = @Schema(implementation = Post.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации")
    })
    @PostMapping
    public ResponseEntity<Post> save(@Valid @RequestBody Post post) {
        postService.save(post);
        var uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(post.getId())
                .toUri();
        return ResponseEntity.status(HttpStatus.CREATED)
                .location(uri)
                .body(post);
    }

    @Operation(summary = "Обновить существующий пост", description = "Обновляет данные поста по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пост успешно обновлён"),
            @ApiResponse(responseCode = "404", description = "Пост с указанным ID не найден"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации")
    })
    @PutMapping
    public ResponseEntity<Void> update(@Valid @RequestBody Post post) {
        if (postService.update(post)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Удалить пост по ID", description = "Удаляет пост по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Пост успешно удалён"),
            @ApiResponse(responseCode = "404", description = "Пост не найден")
    })
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deleteById(@PathVariable("postId") @Min(1) Long postId) {
        if (postService.deleteById(postId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
