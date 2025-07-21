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

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Override
    public Comment getComment(Integer id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Override
    public Comment insertNewComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Comment updateComment(Integer commentId, String content) {

        Comment comment = commentRepository.findById(commentId).orElse(null);
        comment.setContent(content);

        return comment;
    }

    @Override
    public Comment deleteComment(Integer commentId) {

        Comment comment = commentRepository.findById(commentId).orElse(null);
        comment.setIsDeleted(true);

        return comment;
    }

    @Override
    public void likeComment(Comment comment, Member member) {
        if (commentLikeRepository.existsByCommentAndMember(comment, member)
        || comment.getIsDeleted()) {
            System.out.println("예외 처리 예정");
        }

        commentRepository.increaseLikeCount(comment.getId());
        commentLikeRepository.save(CommentLike.builder()
                .comment(comment)
                .member(member)
                .build());
    }

    @Override
    public void unLikeComment(Comment comment, Member member) {
        if (commentLikeRepository.existsByCommentAndMember(comment, member)
                || comment.getIsDeleted() || comment.getLikeCount() ==0 ) {
            System.out.println("예외 처리 예정");
        }

        commentRepository.decreaseLikeCount(comment.getId());
        commentLikeRepository.deleteCommentLikeByCommentAndMember(comment, member);
    }
}
