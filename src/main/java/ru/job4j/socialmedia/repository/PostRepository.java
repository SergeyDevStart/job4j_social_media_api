package ru.job4j.socialmedia.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.job4j.socialmedia.model.Post;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserIdIn(List<Long> ids);

    List<Post> findByUserId(Long id);

    List<Post> findByCreatedGreaterThanEqualAndCreatedLessThanEqual(LocalDateTime startAt, LocalDateTime endAt);

    Page<Post> findByOrderByCreatedDesc(Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("""
            UPDATE Post
            SET title = :title, content = :content
            WHERE id = :id
            """)
    int updateTitleAndContent(
            @Param("title") String title,
            @Param("content") String content,
            @Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query("""
            DELETE File f
            WHERE f.post.id = :postId
            AND f.id = :fileId
            """)
    int deleteFileByPostId(@Param("fileId") Long fileId, @Param("postId") Long postId);

    @Modifying(clearAutomatically = true)
    @Query("""
            DELETE File f
            WHERE f.post.id = :postId
            """)
    int deleteAllFilesByPostId(@Param("postId") Long postId);

    @Modifying(clearAutomatically = true)
    @Query("""
            DELETE Post
            WHERE id = :id
            """)
    int deletePostById(@Param("id") Long id);

    @Query("""
            SELECT f.post FROM FeedEvent f
            WHERE f.user.id = :id
            ORDER BY f.created DESC
            """)
    Page<Post> findSubscribersPostsById(@Param("id") Long id, Pageable pageable);
}
