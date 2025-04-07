package ru.job4j.socialmedia.service.friendship;

import ru.job4j.socialmedia.model.Friendship;
import ru.job4j.socialmedia.model.User;

import java.util.Optional;

public interface FriendshipService {
    Optional<Friendship> findById(Long id);

    Friendship create(User sendUser, User acceptUser);

    void sendRequest(User sendUser, User acceptUser);

    void updateStatus(User sendUser, User acceptUser);

    boolean delete(Friendship friendship);

    void acceptRequest(User sendUser, User acceptUser);

    void removeFromFriends(User removing, User removable);
}
