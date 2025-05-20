package ru.job4j.socialmedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.job4j.socialmedia.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Modifying
    @Query("""
            DELETE User
            WHERE id = :id
            """)
    int deleteUserById(@Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query("""
            UPDATE User u
            SET u.name = :#{#user.name},
            u.email = :#{#user.email},
            u.password = :#{#user.password}
            WHERE u.id = :#{#user.id}
            """)
    int update(@Param("user") User user);

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

    @Query("""
            FROM User u
            LEFT JOIN FETCH u.roles
            WHERE u.email = :email
            """)
    Optional<User> findByEmail(@Param("email") String email);
}
