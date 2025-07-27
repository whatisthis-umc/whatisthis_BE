package umc.demoday.whatisthis.domain.inquiry.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import umc.demoday.whatisthis.global.apiPayload.code.BaseErrorCode;

@Getter
@AllArgsConstructor
public enum InquiryErrorCode implements BaseErrorCode {
    INQUIRY_NOT_FOUND(HttpStatus.OK,"INQUIRY4040", "문의내역을 찾지 못했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
