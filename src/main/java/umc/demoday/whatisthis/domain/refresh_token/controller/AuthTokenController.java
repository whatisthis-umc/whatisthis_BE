package umc.demoday.whatisthis.domain.refresh_token.controller;

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
    public CustomResponse<LoginResDTO> reissue(@RequestHeader("Authorization") String refreshToken) {
        LoginResDTO response = memberAuthService.reissue(refreshToken);
        return CustomResponse.ok(response);
    }
}
