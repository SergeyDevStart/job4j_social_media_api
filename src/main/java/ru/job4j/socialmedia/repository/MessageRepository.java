package ru.job4j.socialmedia.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.job4j.socialmedia.model.Message;

public interface MessageRepository extends ListCrudRepository<Message, Long> {
}
