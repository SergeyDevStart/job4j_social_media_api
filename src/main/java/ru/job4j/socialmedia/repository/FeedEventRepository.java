package ru.job4j.socialmedia.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.job4j.socialmedia.model.FeedEvent;

public interface FeedEventRepository extends ListCrudRepository<FeedEvent, Long> {
}
