package umc.demoday.whatisthis.domain.comment.converter;

import umc.demoday.whatisthis.domain.comment.Comment;
import umc.demoday.whatisthis.domain.comment.dto.CommentRequestDTO;
import umc.demoday.whatisthis.domain.comment.dto.CommentResponseDTO;

public class CommentConverter {

    public static Comment toNewComment(CommentRequestDTO.NewCommentRequestDTO request) {
        return null;
    }

    public static CommentResponseDTO.NewCommentResponseDTO toNewCommentResponseDTO(Comment comment) {
        return null;
    }

    public static CommentResponseDTO.ModifiedCommentResponseDTO toModifiedCommentResponseDTO(Comment comment) {
        return null;
    }

    public static CommentResponseDTO.DeletedCommentResponseDTO toDeletedCommentResponseDTO(Comment comment) {
        return null;
    }

    public static CommentResponseDTO.CommentLikeCountDTO toCommentLikeCountDTO(Comment comment) {
        return null;
    }


}
