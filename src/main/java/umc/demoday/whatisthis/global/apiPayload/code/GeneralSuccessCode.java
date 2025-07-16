package umc.demoday.whatisthis.global.apiPayload.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GeneralSuccessCode implements BaseSuccessCode {

    OK(HttpStatus.OK, "COMMON2000", "성공적으로 처리했습니다."),
    CREATED(HttpStatus.CREATED, "COMMON2010", "성공적으로 생성했습니다."),
    NO_CONTENT_204(HttpStatus.NO_CONTENT, "COMMON2040", "성공했지만 콘텐츠는 없습니다."),

    EMAIL_AUTH_MATCHED(HttpStatus.OK, "EMAIL2000", "인증 코드 번호가 일치합니다."),
    EMAIL_AUTH_SENT(HttpStatus.OK, "EMAIL2001", "입력하신 이메일로 인증 코드가 발송되었습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}