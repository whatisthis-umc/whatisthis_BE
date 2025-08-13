package umc.demoday.whatisthis.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.demoday.whatisthis.domain.member.dto.member.*;
import umc.demoday.whatisthis.domain.member.service.email.PasswordResetService;
import umc.demoday.whatisthis.domain.member.service.member.MemberQueryService;
import umc.demoday.whatisthis.global.apiPayload.CustomResponse;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralSuccessCode;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberFindController {

    private final MemberQueryService memberQueryService;
    private final PasswordResetService passwordResetService;

    @PostMapping("/find-id")
    @Operation(summary = "아이디 찾기 API - by 이정준")
    public ResponseEntity<CustomResponse<FindIdResDTO>> findId(
            @RequestBody @Valid FindIdReqDTO request) {
        FindIdResDTO response = memberQueryService.findMemberIdByEmail(request);
        return ResponseEntity.ok(CustomResponse.ok(response));
    }

    @PostMapping("/reset-password/send-code")
    @Operation(summary = "비밀번호 재설정 - 인증코드 발송 API -by 이정준")
    public CustomResponse<Void> sendResetCode(
            @RequestBody @Valid PasswordResetReqDTO request) {

        passwordResetService.sendResetCode(request.getFullEmail());
        return CustomResponse.onSuccess(GeneralSuccessCode.EMAIL_AUTH_SENT, null);
    }

    @PostMapping("/reset-password/verify-code")
    @Operation(summary = "비밀번호 재설정 - 인증코드 검증(토큰 발급) -by 이정준")
    public ResponseEntity<CustomResponse<Void>> verifyCode(
            @RequestBody @Valid VerifyResetCodeReqDTO request) {

        // 서비스: 코드검증 + (memberId,email) 매칭 + 토큰발급/저장 후 토큰과 TTL 반환
        PasswordResetService.ResetToken rt = passwordResetService.verifyAndIssueResetToken(
                request.getMemberId(),
                request.getFullEmail(),
                request.getCode()
        );

        // HttpOnly 쿠키로 resetToken 전달
        ResponseCookie cookie = ResponseCookie.from("resetToken", rt.value())
                .httpOnly(true)
                .secure(false)        // 로컬 http 테스트면 false, 배포는 true 권장
                .sameSite("Lax")     // 크로스 도메인이면 "None" + secure(true)
                .path("/")
                .maxAge(rt.ttl())
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(CustomResponse.onSuccess(GeneralSuccessCode.EMAIL_AUTH_MATCHED, null));
    }

    // 배포 서버에서는 이거 주석 풀어서 사용
//    @PostMapping("/reset-password")
//    @Operation(summary = "비밀번호 재설정 API -by 이정준")
//    public CustomResponse<Void> resetPassword(
//            @CookieValue(value = "resetToken", required = false) String resetToken,
//            @RequestBody @Valid PasswordChangeReqDTO request) {
//
//        if (resetToken == null || resetToken.isBlank()) {
//            throw new GeneralException(GeneralErrorCode.UNAUTHORIZED_401);
//        }
//
//        passwordResetService.resetPassword(resetToken, request);
//        return CustomResponse.onSuccess(GeneralSuccessCode.PASSWORD_CHANGED, null);
//    }

    @PostMapping("/reset-password")
    @Operation(summary = "비밀번호 재설정 API -by 이정준")
    public CustomResponse<Void> resetPassword(
            @CookieValue(value = "resetToken", required = false) String cookieToken,
            @RequestHeader(value = "X-Reset-Token", required = false) String headerToken,
            @RequestParam(value = "resetToken", required = false) String paramToken,
            @RequestBody @Valid PasswordChangeReqDTO request) {

        // Body에도 resetToken 필드를 추가 (선택값)
        String bodyToken = request.getResetToken();

        // 1) 쿠키 → 2) 헤더 → 3) 쿼리 → 4) 바디 순서로 토큰 찾기
        String resetToken = firstNonBlank(cookieToken, headerToken, paramToken, bodyToken);

        if (resetToken == null || resetToken.isBlank()) {
            throw new GeneralException(GeneralErrorCode.UNAUTHORIZED_401);
        }

        // 서비스 로직 호출 (기존과 동일)
        passwordResetService.resetPassword(resetToken, request);

        return CustomResponse.onSuccess(GeneralSuccessCode.PASSWORD_CHANGED, null);
    }

    private String firstNonBlank(String... vals) {
        for (String v : vals) {
            if (v != null && !v.isBlank()) return v;
        }
        return null;
    }
}