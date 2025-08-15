package umc.demoday.whatisthis.domain.comment_like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.demoday.whatisthis.domain.comment.Comment;
import umc.demoday.whatisthis.domain.comment_like.CommentLike;
import umc.demoday.whatisthis.domain.member.Member;

import java.util.List;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Integer> {
    Integer countByMember(Member member);

    boolean existsByCommentAndMember(Comment comment, Member member);

    void deleteCommentLikeByCommentAndMember(Comment comment, Member member);

    boolean existsByMemberAndComment(Member member, Comment comment);

    @Modifying
    @Query("delete from CommentLike cl where cl.comment.id in :commentIds")
    void deleteByCommentIds(@Param("commentIds") List<Integer> commentIds);
}
