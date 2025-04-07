package ru.job4j.socialmedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.job4j.socialmedia.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
