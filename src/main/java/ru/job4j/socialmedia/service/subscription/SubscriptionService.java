package ru.job4j.socialmedia.service.subscription;

import ru.job4j.socialmedia.model.Subscription;
import ru.job4j.socialmedia.model.User;

import java.util.Optional;

public interface SubscriptionService {
    Subscription create(User subscriber, User subscribedTo);

    Optional<Subscription> findById(Long id);

    boolean delete(Subscription subscription);

    void unsubscribe(User subscriber, User subscribedTo);
}
