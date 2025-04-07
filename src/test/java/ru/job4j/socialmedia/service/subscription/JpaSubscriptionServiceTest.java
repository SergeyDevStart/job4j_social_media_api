package ru.job4j.socialmedia.service.subscription;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.socialmedia.model.Subscription;
import ru.job4j.socialmedia.model.User;
import ru.job4j.socialmedia.repository.FriendshipRepository;
import ru.job4j.socialmedia.repository.SubscriptionRepository;
import ru.job4j.socialmedia.repository.UserRepository;
import ru.job4j.socialmedia.service.friendship.FriendshipService;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class JpaSubscriptionServiceTest {
    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private UserRepository userRepository;
    private User subscriber;
    private User subscribedTo;

    @BeforeEach
    public void setUp() {
        subscriber = new User();
        subscriber.setName("subscriberUser");
        subscriber.setEmail("subscriber@mail.ru");
        subscriber.setPassword("password");
        userRepository.save(subscriber);

        subscribedTo = new User();
        subscribedTo.setName("subscribedToUser");
        subscribedTo.setEmail("subscribedTo@mail.ru");
        subscribedTo.setPassword("password");
        userRepository.save(subscribedTo);
    }

    @Test
    void whenUnsubscribeThenRemovedSubscription() {
        Subscription subscription = subscriptionService.create(subscriber, subscribedTo);

        subscriptionService.unsubscribe(subscriber, subscribedTo);

        assertThat(subscriptionService.findById(subscription.getId())).isEmpty();
    }
}