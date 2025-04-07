package ru.job4j.socialmedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.job4j.socialmedia.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("""
            FROM User WHERE email = :email AND password = :password
            """)
    Optional<User> getByEmailAndPassword(
            @Param("email") String email,
            @Param("password") String password
    );

    @Query("""
            SELECT s.subscriber FROM Subscription s
            WHERE s.subscribedTo.id = :id
            """)
    List<User> findAllSubscriptionsById(@Param("id") Long id);

    @Query("""
            SELECT s.subscriber FROM Subscription s
            WHERE s.subscribedTo.id = :id
            AND s.subscriber.id IN (
                SELECT sub.subscribedTo.id FROM Subscription sub
                WHERE sub.subscriber.id = :id
            )
            """)
    List<User> findAllFriendsById(@Param("id") Long id);
}
