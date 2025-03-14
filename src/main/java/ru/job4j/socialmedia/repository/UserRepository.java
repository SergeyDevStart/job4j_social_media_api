package ru.job4j.socialmedia.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.job4j.socialmedia.model.User;

public interface UserRepository extends ListCrudRepository<User, Long> {
}
