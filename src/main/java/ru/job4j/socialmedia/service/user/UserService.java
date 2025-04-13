package ru.job4j.socialmedia.service.user;

import ru.job4j.socialmedia.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> getByEmailAndPassword(String email, String password);

    boolean update(User user);

    boolean deleteById(Long id);

    List<User> findAll();
}
