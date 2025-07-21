package umc.demoday.whatisthis.domain.comment.converter;

import umc.demoday.whatisthis.domain.comment.Comment;
import umc.demoday.whatisthis.domain.comment.dto.CommentRequestDTO;
import umc.demoday.whatisthis.domain.comment.dto.CommentResponseDTO;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.post.Post;

public class CommentConverter {

    public static Comment toNewComment(CommentRequestDTO.NewCommentRequestDTO request, Post post, Member member, Comment parent) {

        return Comment.builder()
                .content(request.getContent())
                .likeCount(0)
                .isDeleted(false)
                .post(post)
                .member(member)
                .parent(parent)
                .build();
    }

    public static CommentResponseDTO.NewCommentResponseDTO toNewCommentResponseDTO(Comment comment) {
        return CommentResponseDTO.NewCommentResponseDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .nickname(comment.getMember().getNickname())
                .likeCount(comment.getLikeCount())
                .parentCommentId(comment.getParent() != null
                        ? comment.getParent().getId()
                        : null)
                .createdAt(comment.getCreatedAt())
                .build();
    }

    public static CommentResponseDTO.ModifiedCommentResponseDTO toModifiedCommentResponseDTO(Comment comment) {
        return CommentResponseDTO.ModifiedCommentResponseDTO.builder()
                .id(comment.getId())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    public static CommentResponseDTO.DeletedCommentResponseDTO toDeletedCommentResponseDTO(Comment comment) {
        return CommentResponseDTO.DeletedCommentResponseDTO.builder()
                .id(comment.getId())
                .build();
    }

    public static CommentResponseDTO.CommentLikeCountDTO toCommentLikeCountDTO(Comment comment) {
        return CommentResponseDTO.CommentLikeCountDTO.builder()
                .likeCount(comment.getLikeCount())
                .build();
    }


}
