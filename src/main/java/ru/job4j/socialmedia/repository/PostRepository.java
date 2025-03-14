package ru.job4j.socialmedia.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.job4j.socialmedia.model.Post;

public interface PostRepository extends ListCrudRepository<Post, Long> {
}
