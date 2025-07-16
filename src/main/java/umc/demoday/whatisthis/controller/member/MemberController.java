package umc.demoday.whatisthis.controller.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import umc.demoday.whatisthis.dto.email.EmailAuthReqDTO;
import umc.demoday.whatisthis.dto.email.EmailAuthVerifyReqDTO;
import umc.demoday.whatisthis.dto.member.MemberReqDTO;
import umc.demoday.whatisthis.dto.member.MemberResDTO;
import umc.demoday.whatisthis.global.apiPayload.CustomResponse;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralSuccessCode;
import umc.demoday.whatisthis.service.email.EmailAuthService;
import umc.demoday.whatisthis.service.member.MemberCommandService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/members")
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

    @PostMapping("/email-auth/verify")
    @Operation(summary = "이메일 인증 코드 검증 API")
    public CustomResponse<Void> verifyEmailAuthCode(
            @RequestBody @Valid EmailAuthVerifyReqDTO request
    ) {
        boolean result = emailAuthService.verifyAuthCode(request.getEmail(), request.getAuthCode());
        if (result) {
            return CustomResponse.onSuccess(GeneralSuccessCode.EMAIL_AUTH_MATCHED, null);
        } else {
            return CustomResponse.onFailure(
                    GeneralErrorCode.EMAIL_AUTH_CODE_MISMATCH.getCode(),
                    GeneralErrorCode.EMAIL_AUTH_CODE_MISMATCH.getMessage(),
                    null
            );
        }
    }

}
