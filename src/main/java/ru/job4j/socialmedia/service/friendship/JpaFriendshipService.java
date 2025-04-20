package ru.job4j.socialmedia.service.friendship;

import jakarta.persistence.EntityNotFoundException;
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
        var userWithMinId = sendUser.getId() < acceptUser.getId() ? sendUser : acceptUser;
        var userWithMaxId = sendUser.getId() > acceptUser.getId() ? sendUser : acceptUser;
        Friendship friendship = new Friendship();
        friendship.setSendUser(userWithMinId);
        friendship.setAcceptUser(userWithMaxId);
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
    }

    @Override
    @Transactional
    public void acceptRequest(User sendUser, User acceptUser) {
        subscriptionService.create(acceptUser, sendUser);
        this.create(sendUser, acceptUser);
    }

    @Override
    @Transactional
    public void removeFromFriends(User removing, User removable) {
        subscriptionService.unsubscribe(removing, removable);
        var friendship = findFriendshipByUserIds(removing, removable)
                .orElseThrow(() -> new EntityNotFoundException("Entity friendship not found."));
        this.delete(friendship);
    }

    @Override
    public Optional<Friendship> findFriendshipByUserIds(User sendUser, User acceptUser) {
        Long minId = Math.min(sendUser.getId(), acceptUser.getId());
        Long maxId = Math.max(sendUser.getId(), acceptUser.getId());
        return friendshipRepository.findByUserIds(minId, maxId);
    }

    @Override
    @Transactional
    public boolean delete(Friendship friendship) {
        return friendshipRepository.deleteFriendshipById(friendship.getId()) > 0;
    }
}
