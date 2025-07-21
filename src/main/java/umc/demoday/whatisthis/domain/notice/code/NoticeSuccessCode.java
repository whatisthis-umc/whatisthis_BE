package umc.demoday.whatisthis.domain.notice.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import umc.demoday.whatisthis.global.apiPayload.code.BaseSuccessCode;

@Getter
@AllArgsConstructor
public enum NoticeSuccessCode implements BaseSuccessCode {
    NOTICE_OK(HttpStatus.OK,"NOTICE2000", "공지사항을 성공적으로 처리했습니다."),
    NOTICE_CREATE_OK(HttpStatus.CREATED,"NOTICE2010","공지사항이 성공적으로 등록됐습니다."),
    NOTICE_UPDATE_OK(HttpStatus.OK,"NOTICE2001", "공지사항이 성공적으로 수정되었습니다."),
    NOTICE_DELETE_OK(HttpStatus.OK,"NOTICE2002", "공지사항을 성공적으로 삭제했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
