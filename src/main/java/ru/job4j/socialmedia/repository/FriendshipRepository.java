package ru.job4j.socialmedia.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.job4j.socialmedia.model.Friendship;

public interface FriendshipRepository extends ListCrudRepository<Friendship, Long> {
}
