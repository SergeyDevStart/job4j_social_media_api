package ru.job4j.socialmedia.service.post;

import org.springframework.web.multipart.MultipartFile;
import ru.job4j.socialmedia.model.Post;

import java.util.Optional;

public interface PostService {
    Optional<Post> create(Post post, MultipartFile[] files);

    boolean delete(Post post);

    boolean update(Post post, MultipartFile[] files);
}
