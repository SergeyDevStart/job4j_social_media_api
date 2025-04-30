package ru.job4j.socialmedia.service.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.socialmedia.dto.UserDto;
import ru.job4j.socialmedia.mappers.UserMapper;
import ru.job4j.socialmedia.model.User;
import ru.job4j.socialmedia.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class JpaUserService implements UserService {
    private final UserRepository userRepository;
    private final UserMapper mapper;

    @Override
    public User save(UserDto userDto) {
        User user = mapper.toUserFromUserDto(userDto);
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
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
        User user = mapper.toUserFromUserDto(userDto);
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
}
