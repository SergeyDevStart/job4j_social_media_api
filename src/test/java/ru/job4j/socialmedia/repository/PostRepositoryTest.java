package ru.job4j.socialmedia.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.socialmedia.model.Post;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;

    @Test
    void whenSavePostThenFindById() {
        var post = new Post();
        post.setTitle("title");
        post.setContent("content");
        postRepository.save(post);

        var foundPost = postRepository.findById(post.getId());

        assertThat(foundPost).isPresent();
        assertThat(foundPost.get().getTitle()).isEqualTo("title");
    }

    @Test
    void whenFindAllThenFindAllPosts() {
        var postOne = new Post();
        postOne.setTitle("title one");
        postOne.setContent("content one");
        postRepository.save(postOne);
        var postTwo = new Post();
        postTwo.setTitle("title two");
        postTwo.setContent("content two");
        postRepository.save(postTwo);

        var expected = List.of(postOne, postTwo);
        var result = postRepository.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void whenDeleteThenNotFound() {
        var post = new Post();
        post.setTitle("title");
        post.setContent("content");
        postRepository.save(post);
        postRepository.delete(post);
        var foundPost = postRepository.findById(post.getId());

        assertThat(foundPost).isEmpty();
    }
}