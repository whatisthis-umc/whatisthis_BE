package umc.demoday.whatisthis.domain.comment_like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.demoday.whatisthis.domain.comment.Comment;
import umc.demoday.whatisthis.domain.comment_like.CommentLike;
import umc.demoday.whatisthis.domain.member.Member;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Integer> {
    Integer countByMember(Member member);

    boolean existsByCommentAndMember(Comment comment, Member member);

    void deleteCommentLikeByCommentAndMember(Comment comment, Member member);

    boolean existsByMemberAndComment(Member member, Comment comment);
}
