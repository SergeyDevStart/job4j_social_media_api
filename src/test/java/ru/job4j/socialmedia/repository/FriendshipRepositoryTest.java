package ru.job4j.socialmedia.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.socialmedia.model.Friendship;
import ru.job4j.socialmedia.model.User;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class FriendshipRepositoryTest {
    @Autowired
    private FriendshipRepository friendshipRepository;
    @Autowired
    private UserRepository userRepository;
    private User sendUser;
    private User acceptUser;

    @BeforeEach
    public void setUp() {
        sendUser = new User();
        sendUser.setName("sendUserUser");
        sendUser.setEmail("sendUser@mail.ru");
        sendUser.setPassword("password");
        userRepository.save(sendUser);
        acceptUser = new User();
        acceptUser.setName("acceptUserUser");
        acceptUser.setEmail("acceptUser@mail.ru");
        acceptUser.setPassword("password");
        userRepository.save(acceptUser);
    }

    @Test
    void whenSaveThenFindById() {
        var friendship = new Friendship();
        friendship.setSendUser(sendUser);
        friendship.setAcceptUser(acceptUser);
        friendshipRepository.save(friendship);
        var foundFriendship = friendshipRepository.findById(friendship.getId());

        assertThat(foundFriendship).isPresent();
        assertThat(foundFriendship.get()).isEqualTo(friendship);
    }

    @Test
    void whenDeleteThenNotFound() {
        var friendship = new Friendship();
        friendship.setSendUser(sendUser);
        friendship.setAcceptUser(acceptUser);
        friendshipRepository.save(friendship);
        friendshipRepository.delete(friendship);
        var foundFriendship = friendshipRepository.findById(friendship.getId());

        assertThat(foundFriendship).isEmpty();
    }

    @Test
    void whenFindBySendUserIdAndAcceptUserIdThenSuccessfully() {
        var friendship = new Friendship();
        friendship.setSendUser(sendUser);
        friendship.setAcceptUser(acceptUser);
        friendshipRepository.save(friendship);

        var expectedFriendship = friendshipRepository.findBySendUserIdAndAcceptUserId(sendUser.getId(), acceptUser.getId());

        assertThat(expectedFriendship).isPresent();
        assertThat(expectedFriendship.get()).isEqualTo(friendship);
    }
}