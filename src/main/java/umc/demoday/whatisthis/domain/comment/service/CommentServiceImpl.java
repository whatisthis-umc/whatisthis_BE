package umc.demoday.whatisthis.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.demoday.whatisthis.domain.comment.Comment;
import umc.demoday.whatisthis.domain.comment.repository.CommentRepository;
import umc.demoday.whatisthis.domain.comment_like.CommentLike;
import umc.demoday.whatisthis.domain.comment_like.repository.CommentLikeRepository;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post_like.PostLike;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;

import static umc.demoday.whatisthis.domain.comment.code.CommentErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Override
    public Comment getComment(Integer id) {
        return commentRepository.findById(id).orElseThrow(() -> new GeneralException(COMMENT_NOT_FOUND));
    }

    @Override
    public Comment insertNewComment(Comment comment) {

        if(comment.getParent() != null) {
            if (!comment.getPost().equals(comment.getParent().getPost())) {
                throw new GeneralException(PARENT_POST_CONFLICT);
            }
        }

        return commentRepository.save(comment);
    }

    @Override
    public Comment updateComment(Integer commentId, Integer postId, String content, Member member) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new GeneralException(COMMENT_NOT_FOUND));

        if (comment.getIsDeleted()) {
            throw new GeneralException(ALREADY_DELETED_COMMENT);
        }

        if(!comment.getMember().getId().equals(member.getId())) {
            throw new GeneralException(COMMENT_EDIT_FORBIDDEN);
        }

        comment.setContent(content);

        return comment;
    }

    @Override
    public Comment deleteComment(Integer commentId, Integer postId, Member member) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new GeneralException(COMMENT_NOT_FOUND));

        if (comment.getIsDeleted()) {
            throw new GeneralException(ALREADY_DELETED_COMMENT);
        }

        if(!comment.getMember().getId().equals(member.getId())) {
            throw new GeneralException(COMMENT_EDIT_FORBIDDEN);
        }

        comment.setIsDeleted(true);

        return comment;
    }

    @Override
    public void likeComment(Comment comment, Member member) {

        if (comment.getIsDeleted()) {
            throw new GeneralException(ALREADY_DELETED_COMMENT);
        }

        if (commentLikeRepository.existsByMemberAndComment(member, comment)){
            throw new GeneralException(ALREADY_LIKED_COMMENT);
        }

        if (comment.getMember().getId().equals(member.getId())) {
            throw new GeneralException(CANNOT_LIKE_OWN_COMMENT);
        }

        commentRepository.increaseLikeCount(comment.getId());
        commentLikeRepository.save(CommentLike.builder()
                .comment(comment)
                .member(member)
                .build());
    }

    @Override
    public void unLikeComment(Comment comment, Member member) {

        if (comment.getIsDeleted()) {
            throw new GeneralException(ALREADY_DELETED_COMMENT);
        }

        if (!commentLikeRepository.existsByMemberAndComment(member, comment)){
            throw new GeneralException(ALREADY_UNLIKED_COMMENT);
        }

        commentRepository.decreaseLikeCount(comment.getId());
        commentLikeRepository.deleteCommentLikeByCommentAndMember(comment, member);
    }

    @Override
    public void validateCommentByPostId(Comment comment,Integer postId){

        if(!comment.getPost().getId().equals(postId)) {
            throw new GeneralException(COMMENT_POST_CONFLICT);
        }
    }
}
