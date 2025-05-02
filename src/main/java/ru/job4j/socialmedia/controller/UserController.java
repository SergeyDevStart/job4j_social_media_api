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
import ru.job4j.socialmedia.dto.UserDto;
import ru.job4j.socialmedia.dto.UserWithPostsDto;
import ru.job4j.socialmedia.model.User;
import ru.job4j.socialmedia.service.user.UserService;
import ru.job4j.socialmedia.validation.ErrorResponse;
import ru.job4j.socialmedia.validation.ValidationErrorResponse;

@Tag(name = "UserController", description = "UserController management APIs")
@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserService userService;

    @Operation(summary = "Получить пользователя по ID", description = "Возвращает пользователя по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации",
                    content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class)))
    })
    @GetMapping("/{userId}")
    public ResponseEntity<User> get(@PathVariable("userId") @Min(1) Long userId) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Получить пользователя с постами", description = "Возвращает пользователя и его посты по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден",
            content = @Content(schema = @Schema(implementation = UserWithPostsDto.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class)))
    })
    @GetMapping("/{userId}/with-posts")
    public ResponseEntity<UserWithPostsDto> getUserWithPosts(@PathVariable("userId") @Min(1) Long userId) {
        var userWithPosts = userService.getUserWithPostsById(userId);
        return ResponseEntity.ok(userWithPosts);
    }

    @Operation(summary = "Создание пользователя", description = "Создаёт нового пользователя и возвращает его URL в заголовке Location")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно создан",
                    content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации")
    })
    @PostMapping
    public ResponseEntity<UserDto> save(@Valid @RequestBody UserDto userDto) {
        User user = userService.save(userDto);
        var uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();
        return ResponseEntity.status(HttpStatus.CREATED)
                .location(uri)
                .body(userDto);
    }

    @Operation(summary = "Обновить существующего пользователя", description = "Обновляет данные пользователя по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно обновлён"),
            @ApiResponse(responseCode = "404", description = "Пользователь с указанным ID не найден"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации")
    })
    @PutMapping
    public ResponseEntity<Void> update(@Valid @RequestBody UserDto userDto) {
        if (userService.update(userDto)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Удалить пользователя по ID", description = "Удаляет пользователя по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Пользователь успешно удалён"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteById(@PathVariable("userId") @Min(1) Long userId) {
        if (userService.deleteById(userId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
