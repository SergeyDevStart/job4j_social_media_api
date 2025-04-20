package ru.job4j.socialmedia.service.friendship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import ru.job4j.socialmedia.service.subscription.SubscriptionService;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class JpaFriendshipServiceTest {
    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private FriendshipService friendshipService;
    @Autowired
    private FriendshipRepository friendshipRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private UserRepository userRepository;
    private User sendUser;
    private User acceptUser;

    @BeforeEach
    public void setUp() {
        sendUser = new User();
        sendUser.setName("sendUser");
        sendUser.setEmail("sendUser@mail.ru");
        sendUser.setPassword("password");
        userRepository.save(sendUser);

        acceptUser = new User();
        acceptUser.setName("acceptUser");
        acceptUser.setEmail("acceptUser@mail.ru");
        acceptUser.setPassword("password");
        userRepository.save(acceptUser);
    }

    @Test
    void whenSendRequestThenFindById() {
        friendshipService.sendRequest(sendUser, acceptUser);

        var subscriptionOptional = subscriptionRepository.findBySubscriberIdAndSubscribedToId(sendUser.getId(), acceptUser.getId());

        assertThat(subscriptionOptional).isPresent();

        assertThat(subscriptionOptional.get().getSubscriber()).isEqualTo(sendUser);
        assertThat(subscriptionOptional.get().getSubscribedTo()).isEqualTo(acceptUser);
    }

    @Test
    void whenAcceptRequestThenCreateFriendship() {
        friendshipService.sendRequest(sendUser, acceptUser);
        friendshipService.acceptRequest(sendUser, acceptUser);

        var friendshipOptional = friendshipRepository.findByUserIds(sendUser.getId(), acceptUser.getId());
        var subscriptionOptionalOne = subscriptionRepository.findBySubscriberIdAndSubscribedToId(sendUser.getId(), acceptUser.getId());
        var subscriptionOptionalTwo = subscriptionRepository.findBySubscriberIdAndSubscribedToId(acceptUser.getId(), sendUser.getId());

        assertThat(friendshipOptional).isPresent();
        assertThat(subscriptionOptionalOne).isPresent();
        assertThat(subscriptionOptionalTwo).isPresent();
    }

    @Test
    void whenRemoveFromFriendsThenFriendshipNotFound() {
        friendshipService.sendRequest(sendUser, acceptUser);
        friendshipService.acceptRequest(sendUser, acceptUser);
        friendshipService.removeFromFriends(acceptUser, sendUser);

        var friendshipOptional = friendshipRepository.findByUserIds(sendUser.getId(), acceptUser.getId());
        var subscriptionOptionalOne = subscriptionRepository.findBySubscriberIdAndSubscribedToId(sendUser.getId(), acceptUser.getId());
        var subscriptionOptionalTwo = subscriptionRepository.findBySubscriberIdAndSubscribedToId(acceptUser.getId(), sendUser.getId());

        assertThat(friendshipOptional).isEmpty();
        assertThat(subscriptionOptionalOne).isPresent();
        assertThat(subscriptionOptionalTwo).isEmpty();
    }
}