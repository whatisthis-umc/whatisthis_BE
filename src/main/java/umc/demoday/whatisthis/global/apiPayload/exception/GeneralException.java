package umc.demoday.whatisthis.global.apiPayload.exception;

import lombok.Getter;
import umc.demoday.whatisthis.global.apiPayload.code.BaseErrorCode;

@Getter
public class GeneralException extends RuntimeException{

    // 예외에서 발생한 에러의 상세 내용
    private final BaseErrorCode code;

    // 생성자
    public GeneralException(BaseErrorCode code) {
        this.code = code;
    }
}