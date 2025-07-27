package umc.demoday.whatisthis.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.member.dto.member.FindIdReqDTO;
import umc.demoday.whatisthis.domain.member.dto.member.FindIdResDTO;
import umc.demoday.whatisthis.domain.member.dto.member.PasswordResetReqDTO;
import umc.demoday.whatisthis.domain.member.service.email.PasswordResetService;
import umc.demoday.whatisthis.domain.member.service.member.MemberQueryService;
import umc.demoday.whatisthis.global.apiPayload.CustomResponse;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralSuccessCode;

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
    public CustomResponse<Void> sendResetCode (
            @RequestBody @Valid PasswordResetReqDTO request) {

        passwordResetService.sendResetCode(request.getMemberId(), request.getFullEmail());
        return CustomResponse.onSuccess(GeneralSuccessCode.EMAIL_AUTH_SENT, null);
    }
}
