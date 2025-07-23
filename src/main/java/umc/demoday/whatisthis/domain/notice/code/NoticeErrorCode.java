package umc.demoday.whatisthis.domain.notice.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import umc.demoday.whatisthis.global.apiPayload.code.BaseErrorCode;

@Getter
@AllArgsConstructor
public enum NoticeErrorCode implements BaseErrorCode {
    NOTICE_NOT_FOUND(HttpStatus.OK,"NOTICE4040", "공지사항을 찾지 못했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
