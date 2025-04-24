package ru.job4j.socialmedia.dto;

import lombok.*;
import ru.job4j.socialmedia.model.Post;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private List<Post> posts;
}
