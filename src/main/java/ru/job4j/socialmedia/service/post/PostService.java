package ru.job4j.socialmedia.service.post;

import org.springframework.web.multipart.MultipartFile;
import ru.job4j.socialmedia.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Post save(Post post);

    Optional<Post> findById(Long id);

    boolean update(Post post);

    boolean deleteById(Long id);

    List<Post> findAll();

    Optional<Post> create(Post post, MultipartFile[] files);

    boolean delete(Post post);

    boolean update(Post post, MultipartFile[] files);
}
