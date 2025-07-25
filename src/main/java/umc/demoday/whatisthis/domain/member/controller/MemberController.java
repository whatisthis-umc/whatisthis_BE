package umc.demoday.whatisthis.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import umc.demoday.whatisthis.domain.member.dto.email.EmailAuthReqDTO;
import umc.demoday.whatisthis.domain.member.dto.member.MemberReqDTO;
import umc.demoday.whatisthis.domain.member.dto.member.MemberResDTO;
import umc.demoday.whatisthis.global.apiPayload.CustomResponse;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralSuccessCode;
import umc.demoday.whatisthis.domain.member.service.email.EmailAuthService;
import umc.demoday.whatisthis.domain.member.service.member.MemberCommandService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberCommandService memberCommandService;
    private final EmailAuthService emailAuthService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "회원가입 API -by 이정준")
    public CustomResponse<MemberResDTO.JoinResponseDTO> signup(
            @RequestBody @Valid MemberReqDTO.JoinRequestDTO request
    ) {
        MemberResDTO.JoinResponseDTO response = memberCommandService.signUp(request);
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
}
