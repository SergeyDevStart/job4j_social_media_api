package ru.job4j.socialmedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.job4j.socialmedia.model.Subscription;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    @Modifying(clearAutomatically = true)
    @Query("""
            DELETE Subscription
            WHERE id = :id
            """)
    int deleteSubscriptionById(@Param("id") Long id);

    Optional<Subscription> findBySubscriberIdAndSubscribedToId(Long subscriberId, Long subscribedToId);
}
