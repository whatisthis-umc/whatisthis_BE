package umc.demoday.whatisthis.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import umc.demoday.whatisthis.domain.member.dto.login.LoginReqDTO;
import umc.demoday.whatisthis.domain.member.dto.login.LoginResDTO;
import umc.demoday.whatisthis.domain.member.service.member.MemberAuthService;
import umc.demoday.whatisthis.global.CustomUserDetails;
import umc.demoday.whatisthis.global.apiPayload.CustomResponse;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;

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
    public CustomResponse<Void> logout(@AuthenticationPrincipal CustomUserDetails user,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {
        memberAuthService.logout(user.getId(), request, response);
        return CustomResponse.ok(null);
    }
}
