package ru.job4j.socialmedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.job4j.socialmedia.model.Friendship;

import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    @Modifying(clearAutomatically = true)
    @Query("""
            DELETE Friendship
            WHERE id = :id
            """)
    int deleteFriendshipById(@Param("id") Long id);

    Optional<Friendship> findBySendUserIdAndAcceptUserId(Long sendUserId, Long acceptUserId);
}
