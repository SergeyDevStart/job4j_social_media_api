package ru.job4j.socialmedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.job4j.socialmedia.model.FeedEvent;

public interface FeedEventRepository extends JpaRepository<FeedEvent, Long> {
}
