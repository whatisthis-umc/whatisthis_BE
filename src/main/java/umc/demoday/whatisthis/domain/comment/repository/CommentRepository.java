package umc.demoday.whatisthis.domain.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.demoday.whatisthis.domain.comment.Comment;
import umc.demoday.whatisthis.domain.post.Post;

import java.time.LocalDateTime;

public interface CommentRepository extends JpaRepository<Comment, Integer> {


    Integer countByMemberIdAndCreatedAtAfter(Integer memberId, LocalDateTime createdAtAfter);

    Page<Comment> findAllByPost(Post post, Pageable pageable);

    @Modifying
    @Query("UPDATE Comment c SET c.likeCount = c.likeCount + 1 WHERE c.id = :commentId")
    void increaseLikeCount(@Param("commentId") Integer commentId);

    @Modifying
    @Query("UPDATE Comment c SET c.likeCount = c.likeCount - 1 WHERE c.id = :commentId")
    void decreaseLikeCount(@Param("commentId") Integer commentId);
}
