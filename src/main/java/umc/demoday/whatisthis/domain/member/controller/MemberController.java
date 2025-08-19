package umc.demoday.whatisthis.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.member.dto.email.EmailAuthReqDTO;
import umc.demoday.whatisthis.domain.member.dto.member.MemberReqDTO;
import umc.demoday.whatisthis.domain.member.dto.member.MemberResDTO;
import umc.demoday.whatisthis.domain.member.dto.member.SocialLinkReqDTO;
import umc.demoday.whatisthis.domain.member.dto.member.SocialSignupReqDTO;
import umc.demoday.whatisthis.domain.member.repository.MemberRepository;
import umc.demoday.whatisthis.global.CustomUserDetails;
import umc.demoday.whatisthis.global.apiPayload.CustomResponse;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralSuccessCode;
import umc.demoday.whatisthis.domain.member.service.email.EmailAuthService;
import umc.demoday.whatisthis.domain.member.service.member.MemberCommandService;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberCommandService memberCommandService;
    private final EmailAuthService emailAuthService;
    private final MemberRepository memberRepository;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "회원가입 API -by 이정준")
    public CustomResponse<MemberResDTO.JoinResponseDTO> signup(
            @RequestBody @Valid MemberReqDTO.JoinRequestDTO request
    ) {
        MemberResDTO.JoinResponseDTO response = memberCommandService.signUp(request);
        return CustomResponse.created(response);
    }

    @PostMapping("/signup/social")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "소셜 로그인 회원가입 API - by 이정준")
    public CustomResponse<MemberResDTO.JoinResponseDTO> socialSignup(
            @CookieValue(value = "signupToken", required = false) String signupToken,
            @RequestBody @Valid SocialSignupReqDTO request,
            HttpServletResponse response
    ) {
        if (signupToken == null || signupToken.isBlank()) {
            throw new GeneralException(GeneralErrorCode.UNAUTHORIZED_401);
        }
        MemberResDTO.JoinResponseDTO out = memberCommandService.signUpSocialByCookieToken(signupToken, request);

        // 1회용 토큰 삭제
        ResponseCookie delete = ResponseCookie.from("signupToken", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, delete.toString());

        return CustomResponse.created(out);
    }

    @PostMapping("/email-auth")
    @Operation(summary = "이메일 인증 코드 전송 API -by 이정준")
    public CustomResponse<Void> sendEmailAuthCode(
            @RequestBody @Valid EmailAuthReqDTO request
    ) {
        emailAuthService.sendAuthCode(request.getEmail());
        return CustomResponse.onSuccess(GeneralSuccessCode.EMAIL_AUTH_SENT, null);
    }

    @PostMapping("/link-social")
    @Operation(summary = "기존 계정과 연동 API - by 이정준 (linkToken 쿠키 기반, 최소 변경)")
    public CustomResponse<Void> linkSocial(
            @CookieValue(value = "linkToken", required = false) String linkToken,
            HttpServletResponse res
    ) {
        if (linkToken == null || linkToken.isBlank()) {
            throw new GeneralException(GeneralErrorCode.UNAUTHORIZED_401);
        }

        // 서비스에 토큰 처리 전부 위임 → access/refresh만 받아옴
        MemberResDTO.IssuedTokens tokens = memberCommandService.linkSocialByCookieToken(linkToken);

        // HttpOnly/SameSite=None/secure 쿠키로 내려줌 (문자열로 간단히)
        setCookie(res, "accessToken",  tokens.accessToken(),  60 * 60);          // 1시간
        setCookie(res, "refreshToken", tokens.refreshToken(), 60 * 60 * 24 * 7); // 7일

        // 1회용 linkToken 삭제
        clearCookie(res, "linkToken");

        return CustomResponse.onSuccess(GeneralSuccessCode.SOCIAL_LINKED, null);
    }

    // ====== 헬퍼: ResponseCookie 안 쓰고 문자열로 ======
    private void setCookie(HttpServletResponse res, String name, String value, int maxAgeSeconds) {
        String cookie = name + "=" + value
                + "; Path=/"
                + "; Domain=.whatisthis.co.kr"
                + "; Max-Age=" + maxAgeSeconds
                + "; HttpOnly"
                + "; Secure"
                + "; SameSite=None";
        res.addHeader("Set-Cookie", cookie);
    }

    private void clearCookie(HttpServletResponse res, String name) {
        String cookie = name + "=; Path=/; Domain=.whatisthis.co.kr; Max-Age=0; HttpOnly; Secure; SameSite=None";
        res.addHeader("Set-Cookie", cookie);
    }
}
