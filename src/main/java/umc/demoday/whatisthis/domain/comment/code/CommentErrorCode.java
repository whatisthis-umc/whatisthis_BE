package umc.demoday.whatisthis.domain.comment.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import umc.demoday.whatisthis.global.apiPayload.code.BaseErrorCode;

@Getter
@AllArgsConstructor
public enum CommentErrorCode implements BaseErrorCode {

    COMMENT_POST_CONFLICT(HttpStatus.BAD_REQUEST, "COMMENT4000", "댓글이 해당 게시물에 속하지 않습니다."),
    ALREADY_DELETED_COMMENT(HttpStatus.BAD_REQUEST, "COMMENT4001","이미 삭제된 댓글입니다."),
    COMMENT_EDIT_FORBIDDEN(HttpStatus.FORBIDDEN, "COMMENT4031", "본인이 작성한 댓글만 수정 혹은 삭제할 수 있습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND,"COMMENT4041","존재하지 않는 댓글입니다."),
    PARENT_POST_CONFLICT(HttpStatus.CONFLICT,"COMMENT4091","부모 댓글은 동일한 게시물에 있어야합니다."),
    ALREADY_LIKED_COMMENT(HttpStatus.CONFLICT,"COMMENT4092", "이미 좋아요 등록한 댓글입니다."),
    ALREADY_UNLIKED_COMMENT(HttpStatus.CONFLICT,"COMMENT4093", "이미 좋아요가 없는 댓글입니다."),
    CANNOT_LIKE_OWN_COMMENT(HttpStatus.FORBIDDEN, "COMMENT4031", "자신의 댓글은 좋아요할 수 없습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
