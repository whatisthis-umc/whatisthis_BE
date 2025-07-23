package umc.demoday.whatisthis.domain.qna.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import umc.demoday.whatisthis.global.apiPayload.code.BaseErrorCode;

@Getter
@AllArgsConstructor
public enum QnaErrorCode implements BaseErrorCode {
    NOTICE_NOT_FOUND(HttpStatus.OK,"QNA4040", "qna를 찾지 못했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
