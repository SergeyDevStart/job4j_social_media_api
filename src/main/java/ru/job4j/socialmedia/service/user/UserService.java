package ru.job4j.socialmedia.service.user;

import ru.job4j.socialmedia.dto.UserDto;
import ru.job4j.socialmedia.dto.UserWithPostsDto;
import ru.job4j.socialmedia.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User save(UserDto userDto);

    User findById(Long id);

    User getByEmailAndPassword(String email, String password);

    boolean update(UserDto userDto);

    boolean deleteById(Long id);

    List<User> findAll();

    List<UserWithPostsDto> getUsersWithPostsByUserIds(List<Long> ids);

    UserWithPostsDto getUserWithPostsById(Long id);
}
