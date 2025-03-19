package ru.job4j.socialmedia.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.socialmedia.model.Subscription;
import ru.job4j.socialmedia.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class SubscriptionRepositoryTest {
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
    void whenSaveThenFindById() {
        var subscription = new Subscription();
        subscription.setSubscriber(subscriber);
        subscription.setSubscribedTo(subscribedTo);
        subscriptionRepository.save(subscription);

        var foundSubscription = subscriptionRepository.findById(subscription.getId());
        assertThat(foundSubscription).isPresent();

        assertThat(foundSubscription.get()).isEqualTo(subscription);
    }
    
    @Test
    void whenFindAllThenFindAllSubscriptions() {
        var subscriptionOne = new Subscription();
        subscriptionOne.setSubscriber(subscriber);
        subscriptionOne.setSubscribedTo(subscribedTo);
        subscriptionRepository.save(subscriptionOne);

        var subscriptionTwo = new Subscription();
        subscriptionTwo.setSubscriber(subscribedTo);
        subscriptionTwo.setSubscribedTo(subscriber);
        subscriptionRepository.save(subscriptionTwo);

        var expected = List.of(subscriptionOne, subscriptionTwo);
        var result = subscriptionRepository.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void whenDeleteThenNotFound() {
        var subscription = new Subscription();
        subscription.setSubscriber(subscriber);
        subscription.setSubscribedTo(subscribedTo);
        subscriptionRepository.save(subscription);
        subscriptionRepository.delete(subscription);

        var foundSubscription = subscriptionRepository.findById(subscription.getId());
        assertThat(foundSubscription).isEmpty();
    }
}