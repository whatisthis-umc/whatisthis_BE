package umc.demoday.whatisthis.domain.answer.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import umc.demoday.whatisthis.global.apiPayload.code.BaseSuccessCode;

@Getter
@AllArgsConstructor
public enum AnswerSuccessCode implements BaseSuccessCode {
    ANSWER_REGISTER_OK(HttpStatus.CREATED, "ANSWER2010", "답변이 성공적으로 등록됐습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}

