package umc.demoday.whatisthis.domain.report.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import umc.demoday.whatisthis.global.apiPayload.code.BaseErrorCode;

@Getter
@AllArgsConstructor
public enum ReportErrorCode implements BaseErrorCode {

    REPORT_NOT_FOUND(HttpStatus.NOT_FOUND,"REPORT4041","존재하지 않는 신고입니다."),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
