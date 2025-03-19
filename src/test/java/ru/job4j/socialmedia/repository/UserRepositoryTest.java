package ru.job4j.socialmedia.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.socialmedia.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void whenSaveUserThenFindById() {
        var user = new User();
        user.setName("name");
        user.setEmail("test@mail.ru");
        user.setPassword("password");
        userRepository.save(user);

        var foundUser = userRepository.findById(user.getId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void whenFindAllThenFindAllUsers() {
        var userOne = new User();
        userOne.setName("user one");
        userOne.setEmail("testOne@mail.ru");
        userOne.setPassword("password");
        var userTwo = new User();
        userTwo.setName("user two");
        userTwo.setEmail("testTwo@mail.ru");
        userTwo.setPassword("password");
        userRepository.save(userOne);
        userRepository.save(userTwo);
        var resultList = List.of(userOne, userTwo);

        assertThat(userRepository.findAll()).isEqualTo(resultList);
    }

    @Test
    void whenDeleteThenNotFound() {
        var user = new User();
        user.setName("name");
        user.setEmail("test@mail.ru");
        user.setPassword("password");
        userRepository.save(user);

        userRepository.delete(user);

        assertThat(userRepository.findById(user.getId())).isEmpty();
    }
}