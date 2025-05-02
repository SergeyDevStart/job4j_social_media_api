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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.socialmedia.dto.UserWithPostsDto;
import ru.job4j.socialmedia.model.User;
import ru.job4j.socialmedia.service.user.UserService;

import java.util.List;

@Tag(name = "UsersController", description = "UsersController management APIs")
@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UsersController {
    private final UserService userService;

    @Operation(summary = "Получить список пользователей", description = "Возвращает список всех пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получен список всех пользователей",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = User.class))))
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAll() {
        return userService.findAll();
    }

    @Operation(summary = "Получить пользователей с их постами", description = "Загружает список пользователей с их постами по переданному списку ID пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получен список пользователей с постами",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserWithPostsDto.class)))),
            @ApiResponse(responseCode = "404", description = "Пользователи не найдены")
    })
    @GetMapping("/with-posts")
    public ResponseEntity<List<UserWithPostsDto>> getUsersWithPostsByUserIds(@RequestParam(name = "ids")
                                                                             @Parameter(
                                                                                     description = "Список ID пользователей",
                                                                                     example = "1,2,3"
                                                                             )
                                                                             List<Long> ids) {
        var usersWithPosts = userService.getUsersWithPostsByUserIds(ids);
        if (!usersWithPosts.isEmpty()) {
            return ResponseEntity.ok(usersWithPosts);
        }
        return ResponseEntity.notFound().build();
    }
}
