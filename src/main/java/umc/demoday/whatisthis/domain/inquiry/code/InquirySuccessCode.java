package umc.demoday.whatisthis.domain.inquiry.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import umc.demoday.whatisthis.global.apiPayload.code.BaseSuccessCode;

@Getter
@AllArgsConstructor
public enum InquirySuccessCode implements BaseSuccessCode {
    INQUIRY_OK(HttpStatus.OK,"INQUIRY2000", "문의내역을 성공적으로 처리했습니다."),
    INQUIRY_CREATE_OK(HttpStatus.CREATED,"INQUIRY2010","문의내역이 성공적으로 등록됐습니다."),
    INQUIRY_UPDATE_OK(HttpStatus.OK,"INQUIRY2001", "문의내역이 성공적으로 수정되었습니다."),
    INQUIRY_DELETE_OK(HttpStatus.OK,"INQUIRY2002", "문의내역을 성공적으로 삭제했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
