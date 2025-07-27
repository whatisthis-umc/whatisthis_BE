package umc.demoday.whatisthis.global.apiPayload.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GeneralErrorCode implements BaseErrorCode {

    BAD_REQUEST_400(HttpStatus.BAD_REQUEST, "COMMON4000", "잘못된 요청입니다"),
    MEMBER_400(HttpStatus.BAD_REQUEST, "MEMBER4000", "잘못된 요청입니다"),
    UNAUTHORIZED_401(HttpStatus.UNAUTHORIZED, "COMMON4010", "인증이 필요합니다"),
    FORBIDDEN_403(HttpStatus.FORBIDDEN, "COMMON4030", "접근이 금지되었습니다"),
    NOT_FOUND_404(HttpStatus.NOT_FOUND, "COMMON4040", "요청한 자원을 찾을 수 없습니다"),
    INTERNAL_SERVER_ERROR_500(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON5000", "서버 내부 오류가 발생했습니다"),

    // 멤버 관련 에러
    ALREADY_EXIST_MEMBER_ID(HttpStatus.BAD_REQUEST, "MEMBER4001", "이미 사용 중인 아이디입니다."),
    ALREADY_EXIST_NICKNAME(HttpStatus.BAD_REQUEST, "MEMBER4002", "이미 사용 중인 닉네임입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER4040", "존재하지 않는 회원입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "MEMBER4010", "비밀번호가 일치하지 않습니다."),
    MEMBER_NOT_FOUND_BY_EMAIL(HttpStatus.NOT_FOUND, "MEMBER4041", "해당 이메일로 가입된 회원이 존재하지 않습니다"),
    PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "MEMBER4042", "비밀번호가 일치하지 않습니다."),
    PASSWORD_SAME_AS_BEFORE(HttpStatus.BAD_REQUEST, "MEMBER4043", "기존 비밀번호와 동일한 비밀번호는 사용할 수 없습니다."),


    // 관리자 관련 에러
    ADMIN_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "ADMIN4040", "존재하지 않는 관리자 ID입니다."),
    ADMIN_PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "ADMIN4010", "비밀번호가 일치하지 않습니다."),


    // 이메일 관련 에러
    EMAIL_AUTH_CODE_MISMATCH(HttpStatus.BAD_REQUEST, "EMAIL4000", "인증 코드 번호가 일치하지 않습니다."),

    // 약관 관련 에러
    TERMS_REQUIRED(HttpStatus.BAD_REQUEST, "TERMS4000", "필수 항목에 체크해주세요.");
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}