package ru.job4j.socialmedia.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.socialmedia.dto.UserDto;
import ru.job4j.socialmedia.model.Post;
import ru.job4j.socialmedia.service.post.PostService;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
@Slf4j
public class PostsController {
    private final PostService postService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Post> getAll() {
        return postService.findAll();
    }

    @GetMapping("/by-users")
    public ResponseEntity<List<UserDto>> getUsersWithPostsByUserIds(@RequestParam  List<Long> ids) {
        var usersWithPosts = postService.getUsersWithPostsByUserIds(ids);
        if (!usersWithPosts.isEmpty()) {
            return ResponseEntity.ok(usersWithPosts);
        }
        return ResponseEntity.notFound().build();
    }
}
