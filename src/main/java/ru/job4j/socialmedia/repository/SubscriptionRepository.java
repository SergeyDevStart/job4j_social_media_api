package ru.job4j.socialmedia.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.job4j.socialmedia.model.Subscription;

public interface SubscriptionRepository extends ListCrudRepository<Subscription, Long> {
}
