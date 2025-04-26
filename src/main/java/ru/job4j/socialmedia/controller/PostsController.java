package ru.job4j.socialmedia.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.socialmedia.dto.UserDto;
import ru.job4j.socialmedia.model.Post;
import ru.job4j.socialmedia.model.User;
import ru.job4j.socialmedia.service.post.PostService;

import java.util.List;

@Tag(name = "PostsController", description = "PostsController management APIs")
@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
@Slf4j
public class PostsController {
    private final PostService postService;

    @Operation(summary = "Получить список постов", description = "Возвращает список всех постов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получен список всех постов",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Post.class))))
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Post> getAll() {
        return postService.findAll();
    }

    @Operation(summary = "Получить пользователей с их постами", description = "Загружает список пользователей с их постами по переданному списку ID пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получен список пользователей с постами",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))),
            @ApiResponse(responseCode = "404", description = "Пользователи не найдены")
    })
    @GetMapping("/by-users")
    public ResponseEntity<List<UserDto>> getUsersWithPostsByUserIds(@RequestParam(name = "ids")
                                                                        @Parameter(
                                                                                description = "Список ID пользователей",
                                                                                example = "1,2,3"
                                                                        )
                                                                        List<Long> ids) {
        var usersWithPosts = postService.getUsersWithPostsByUserIds(ids);
        if (!usersWithPosts.isEmpty()) {
            return ResponseEntity.ok(usersWithPosts);
        }
        return ResponseEntity.notFound().build();
    }
}
