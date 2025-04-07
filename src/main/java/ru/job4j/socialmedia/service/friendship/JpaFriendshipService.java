package ru.job4j.socialmedia.service.friendship;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.socialmedia.model.Friendship;
import ru.job4j.socialmedia.model.User;
import ru.job4j.socialmedia.repository.FriendshipRepository;
import ru.job4j.socialmedia.service.subscription.SubscriptionService;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class JpaFriendshipService implements FriendshipService {
    private final SubscriptionService subscriptionService;
    private final FriendshipRepository friendshipRepository;

    @Override
    public Friendship create(User sendUser, User acceptUser) {
        Friendship friendship = new Friendship();
        friendship.setSendUser(sendUser);
        friendship.setAcceptUser(acceptUser);
        return friendshipRepository.save(friendship);
    }

    @Override
    public Optional<Friendship> findById(Long id) {
        return friendshipRepository.findById(id);
    }

    @Override
    @Transactional
    public void sendRequest(User sendUser, User acceptUser) {
        subscriptionService.create(sendUser, acceptUser);
        this.create(sendUser, acceptUser);
    }

    @Override
    @Transactional
    public void acceptRequest(User sendUser, User acceptUser) {
        subscriptionService.create(acceptUser, sendUser);
        this.updateStatus(sendUser, acceptUser);
    }

    @Override
    @Transactional
    public void updateStatus(User sendUser, User acceptUser) {
        friendshipRepository.findBySendUserIdAndAcceptUserId(sendUser.getId(), acceptUser.getId())
                .ifPresentOrElse(
                        friendship -> {
                            friendship.setStatus(!friendship.isStatus());
                            friendshipRepository.save(friendship);
                            log.info("Status updated successfully: {}", friendship.getId());
                        },
                        () -> log.warn("Entity friendship not found.")
                );
    }

    @Override
    @Transactional
    public void removeFromFriends(User removing, User removable) {
        subscriptionService.unsubscribe(removing, removable);
        updateStatus(removing, removable);
    }

    @Override
    @Transactional
    public boolean delete(Friendship friendship) {
        return friendshipRepository.deleteFriendshipById(friendship.getId()) > 0;
    }
}
