package umc.demoday.whatisthis.domain.post.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import umc.demoday.whatisthis.global.apiPayload.code.BaseErrorCode;

@Getter
@AllArgsConstructor
public enum PostErrorCode implements BaseErrorCode {

    INVALID_PAGE_REQUEST(HttpStatus.BAD_REQUEST,"POST4001", "page와 size의 값은 1 이상이어야 합니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND,"POST4041","존재하지 않는 게시물입니다."),
    ALREADY_LIKED_POST(HttpStatus.CONFLICT,"POST4091", "이미 좋아요 등록한 게시물입니다."),
    ALREADY_UNLIKED_POST(HttpStatus.CONFLICT,"POST4092", "이미 좋아요가 없는 게시물입니다."),
    CANNOT_LIKE_OWN_POST(HttpStatus.FORBIDDEN, "POST4031", "자신의 게시물은 좋아요할 수 없습니다."),
    NOT_COMMUNITY(HttpStatus.BAD_REQUEST,"POST4002", "커뮤니티 카테고리가 아닙니다.")

    //INVALID_CATEGORY(HttpStatus.BAD_REQUEST,"")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
