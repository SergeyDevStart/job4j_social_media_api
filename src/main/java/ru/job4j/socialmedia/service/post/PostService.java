package ru.job4j.socialmedia.service.post;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.socialmedia.dto.PostDto;
import ru.job4j.socialmedia.dto.UserWithPostsDto;
import ru.job4j.socialmedia.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Post save(Post post);

    Optional<Post> findById(Long id);

    boolean update(Post post);

    boolean deleteById(Long id);

    List<Post> findAll();

    List<UserWithPostsDto> getUsersWithPostsByUserIds(List<Long> ids);

    Post create(PostDto postDto, MultipartFile[] files);

    boolean delete(Post post);

    boolean update(PostDto postDto, MultipartFile[] files);
}
