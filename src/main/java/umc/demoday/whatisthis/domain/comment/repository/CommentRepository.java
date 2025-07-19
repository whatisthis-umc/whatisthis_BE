package umc.demoday.whatisthis.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.demoday.whatisthis.domain.comment.Comment;

import java.time.LocalDateTime;

public interface CommentRepository extends JpaRepository<Comment, Integer> {


    Integer countByMemberIdAndCreatedAtAfter(Integer memberId, LocalDateTime createdAtAfter);
}
