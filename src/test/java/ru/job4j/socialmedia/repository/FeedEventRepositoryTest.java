package ru.job4j.socialmedia.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.socialmedia.model.FeedEvent;
import ru.job4j.socialmedia.model.Post;
import ru.job4j.socialmedia.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class FeedEventRepositoryTest {
    @Autowired
    private FeedEventRepository feedEventRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    private User user;
    private Post post;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("name");
        user.setEmail("@mail.ru");
        user.setPassword("password");
        userRepository.save(user);
        post = new Post();
        post.setTitle("title");
        post.setContent("content");
        postRepository.save(post);
    }

    @Test
    void whenSaveThenFindById() {
        var feedEvent = new FeedEvent();
        feedEvent.setUser(user);
        feedEvent.setPost(post);
        feedEventRepository.save(feedEvent);

        var foundFeedEvent = feedEventRepository.findById(feedEvent.getId());

        assertThat(foundFeedEvent).isPresent();
        assertThat(foundFeedEvent.get()).isEqualTo(feedEvent);
    }

    @Test
    void whenFindAllThenFindAllFeedEvents() {
        var feedEventOne = new FeedEvent();
        feedEventOne.setUser(user);
        feedEventOne.setPost(post);
        feedEventRepository.save(feedEventOne);
        var feedEventTwo = new FeedEvent();
        var postTwo = new Post();
        postTwo.setTitle("title two");
        postTwo.setContent("content two");
        postRepository.save(postTwo);
        feedEventTwo.setUser(user);
        feedEventTwo.setPost(postTwo);
        feedEventRepository.save(feedEventTwo);

        var expected = List.of(feedEventOne, feedEventTwo);
        var result = feedEventRepository.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void whenDeleteThenNotFound() {
        var feedEvent = new FeedEvent();
        feedEvent.setUser(user);
        feedEvent.setPost(post);
        feedEventRepository.save(feedEvent);
        feedEventRepository.delete(feedEvent);

        assertThat(feedEventRepository.findById(feedEvent.getId())).isEmpty();
    }
}