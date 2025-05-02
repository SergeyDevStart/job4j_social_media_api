package ru.job4j.socialmedia.service.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.socialmedia.dto.UserWithPostsDto;
import ru.job4j.socialmedia.model.Post;
import ru.job4j.socialmedia.model.User;
import ru.job4j.socialmedia.repository.PostRepository;
import ru.job4j.socialmedia.repository.UserRepository;
import ru.job4j.socialmedia.service.post.JpaPostService;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class JpaUserServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PostRepository postRepository;

    @Test
    void whenGetUsersWithPostsByUserIdsThenGetUserDtoList() {
        User userOne = new User();
        userOne.setName("nameOne");
        userOne.setEmail("emailOne@mail.ru");
        userOne.setPassword("pass");

        User userTwo = new User();
        userTwo.setName("userTwo");
        userTwo.setEmail("emailTwo@mail.ru");
        userTwo.setPassword("pass");

        userRepository.save(userOne);
        userRepository.save(userTwo);
        Post postOne = new Post();

        postOne.setTitle("T1");
        postOne.setContent("C1");
        postOne.setUser(userOne);

        Post postTwo = new Post();
        postTwo.setTitle("T2");
        postTwo.setContent("C2");
        postTwo.setUser(userTwo);

        postRepository.save(postOne);
        postRepository.save(postTwo);

        List<Long> userIds = List.of(userOne.getId(), userTwo.getId());

        List<UserWithPostsDto> result = userService.getUsersWithPostsByUserIds(userIds);

        assertThat(result).hasSize(2);
    }
}