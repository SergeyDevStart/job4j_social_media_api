package ru.job4j.socialmedia.service.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.socialmedia.dto.UserDto;
import ru.job4j.socialmedia.dto.UserWithPostsDto;
import ru.job4j.socialmedia.mappers.UserMapper;
import ru.job4j.socialmedia.model.Post;
import ru.job4j.socialmedia.model.User;
import ru.job4j.socialmedia.repository.PostRepository;
import ru.job4j.socialmedia.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class JpaUserService implements UserService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final UserMapper userMapper;

    @Override
    public User save(UserDto userDto) {
        User user = userMapper.toUserFromUserDto(userDto);
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity user not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public User getByEmailAndPassword(String email, String password) {
        return userRepository.getByEmailAndPassword(email, password)
                .orElseThrow(() -> new EntityNotFoundException("Entity user not found"));
    }

    @Transactional
    @Override
    public boolean update(UserDto userDto) {
        User user = userMapper.toUserFromUserDto(userDto);
        return userRepository.update(user) > 0;
    }

    @Transactional
    @Override
    public boolean deleteById(Long id) {
        return userRepository.deleteUserById(id) > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserWithPostsDto> getUsersWithPostsByUserIds(List<Long> ids) {
        List<User> users = userRepository.findAllById(ids);
        List<Post> posts = postRepository.findByUserIdIn(ids);
        Map<Long, List<Post>> postsByUserId = posts.stream()
                .collect(Collectors.groupingBy(post -> post.getUser().getId()));
        return users.stream()
                .map(user -> {
                    List<Post> userPosts = postsByUserId.getOrDefault(user.getId(), List.of());
                    return userMapper.getUserDtoWithPosts(user, userPosts);
                }).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserWithPostsDto getUserWithPostsById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity user not found"));
        List<Post> posts = postRepository.findByUserId(id);
        return userMapper.getUserDtoWithPosts(user, posts);
    }
}
