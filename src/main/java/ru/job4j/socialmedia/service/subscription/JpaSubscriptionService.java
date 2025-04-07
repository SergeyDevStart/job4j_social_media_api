package ru.job4j.socialmedia.service.subscription;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.socialmedia.model.Subscription;
import ru.job4j.socialmedia.model.User;
import ru.job4j.socialmedia.repository.SubscriptionRepository;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class JpaSubscriptionService implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    @Override
    public Subscription create(User subscriber, User subscribedTo) {
        Subscription subscription = new Subscription();
        subscription.setSubscriber(subscriber);
        subscription.setSubscribedTo(subscribedTo);
        return subscriptionRepository.save(subscription);
    }

    @Override
    public Optional<Subscription> findById(Long id) {
        return subscriptionRepository.findById(id);
    }

    @Override
    @Transactional
    public boolean delete(Subscription subscription) {
        return subscriptionRepository.deleteSubscriptionById(subscription.getId()) > 0;
    }

    @Override
    @Transactional
    public void unsubscribe(User subscriber, User subscribedTo) {
        subscriptionRepository.findBySubscriberIdAndSubscribedToId(subscriber.getId(), subscribedTo.getId())
                .ifPresentOrElse(
                        subscription -> {
                            delete(subscription);
                            log.info("The unsubscribe operation was successful.");
                        },
                        () -> log.warn("Entity subscription not found.")
                );
    }
}
