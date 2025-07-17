package umc.demoday.whatisthis.domain.refresh_token.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.demoday.whatisthis.domain.member.dto.login.LoginResDTO;
import umc.demoday.whatisthis.domain.member.service.member.MemberAuthService;
import umc.demoday.whatisthis.global.apiPayload.CustomResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthTokenController {

    private final MemberAuthService memberAuthService;

    @PostMapping("/reissue")
    @Operation(summary = "accessToken 재발급 API -by 이정준", security = {@SecurityRequirement(name = "")})
    public CustomResponse<LoginResDTO> reissue(@RequestHeader("Refresh-Token") String refreshToken) {
        LoginResDTO response = memberAuthService.reissue(refreshToken);
        return CustomResponse.ok(response);
    }
}
