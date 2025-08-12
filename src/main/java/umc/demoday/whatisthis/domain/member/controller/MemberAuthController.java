package umc.demoday.whatisthis.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import umc.demoday.whatisthis.domain.member.dto.login.LoginReqDTO;
import umc.demoday.whatisthis.domain.member.dto.login.LoginResDTO;
import umc.demoday.whatisthis.domain.member.service.member.MemberAuthService;
import umc.demoday.whatisthis.global.CustomUserDetails;
import umc.demoday.whatisthis.global.apiPayload.CustomResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberAuthController {

    private final MemberAuthService memberAuthService;

    @PostMapping("/login")
    @Operation(summary = "일반 사용자 로그인 API -by 이정준")
    public CustomResponse<LoginResDTO> login(
            @RequestBody @Valid LoginReqDTO request
    ) {
        LoginResDTO response = memberAuthService.login(request);
        return CustomResponse.ok(response);
    }

    @PostMapping("/reissue")
    @Operation(summary = "accessToken 재발급 API -by 이정준", security = {@SecurityRequirement(name = "")})
    public CustomResponse<LoginResDTO> reissue(@RequestHeader("Refresh-Token") String refreshToken) {
        LoginResDTO response = memberAuthService.reissue(refreshToken);
        return CustomResponse.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "일반 사용자 로그아웃 API -by 이정준")
    public CustomResponse<Void> logout(@AuthenticationPrincipal CustomUserDetails member) {
        memberAuthService.logout(member.getId());
        return CustomResponse.ok(null);
    }
}
