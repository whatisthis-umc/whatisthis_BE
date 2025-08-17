package umc.demoday.whatisthis.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
            @RequestBody @Valid SocialSignupReqDTO request
    ) {
        MemberResDTO.JoinResponseDTO response = memberCommandService.signUpSocial(request);
        return CustomResponse.created(response);
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
    @Operation(summary = "기존 계정과 연동 API -by 이정준")
    public CustomResponse<Void> linkSocial(
            @RequestBody @Valid SocialLinkReqDTO request
    ) {
        memberCommandService.linkSocial(request);
        return CustomResponse.onSuccess(GeneralSuccessCode.SOCIAL_LINKED, null);
    }

    @GetMapping("/me")
    @Operation(summary = "현재 로그인 상태 확인 API -by 이정준")
    public CustomResponse<MemberMeRes> me(@AuthenticationPrincipal CustomUserDetails principal) {
        if (principal == null) {
            throw new GeneralException(GeneralErrorCode.UNAUTHORIZED_401); // or 401 반환
        }
        Member m = memberRepository.findById(principal.getId()).orElse(null);
        MemberMeRes res = (m == null)
                ? new MemberMeRes(principal.getId(), principal.getUsername(), null, null, principal.getRole())
                : new MemberMeRes(m.getId(), m.getMemberId(), m.getEmail(), m.getNickname(), "ROLE_USER");
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, res);
    }

    public record MemberMeRes(Integer id, String memberId, String email, String nickname, String role) {}
}
