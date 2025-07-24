package umc.demoday.whatisthis.domain.comment.service;

import umc.demoday.whatisthis.domain.comment.Comment;
import umc.demoday.whatisthis.domain.member.Member;

public interface CommentService {

    Comment getComment(Integer id);
    Comment insertNewComment(Comment comment);
    Comment updateComment(Integer commentId, Integer postId, String content, Member member);
    Comment deleteComment(Integer commentId, Integer postId, Member member);

    void likeComment(Comment comment, Member member);
    void unLikeComment(Comment comment, Member member);
}
