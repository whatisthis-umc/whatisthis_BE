package umc.demoday.whatisthis.domain.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.enums.Category;

import java.time.LocalDateTime;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findAllByCategory(Category category, Pageable pageable);
    Page<Post> findByCreatedAtAfter(LocalDateTime createdAtAfter, Pageable pageable);

    Integer countByMemberIdAndCreatedAtAfter(Integer memberId, LocalDateTime createdAtAfter);

    @Modifying
    @Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :postId")
    void increaseViewCount(@Param("postId") Integer postId);

    @Modifying
    @Query("UPDATE Post p SET p.likeCount = p.likeCount + 1 WHERE p.id = :postId")
    void increaseLikeCount(@Param("postId") Integer postId);

    @Modifying
    @Query("UPDATE Post p SET p.likeCount = p.likeCount - 1 WHERE p.id = :postId")
    void decreaseLikeCount(@Param("postId") Integer postId);
}
