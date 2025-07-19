package umc.demoday.whatisthis.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.demoday.whatisthis.domain.member.dto.login.LoginReqDTO;
import umc.demoday.whatisthis.domain.member.dto.login.LoginResDTO;
import umc.demoday.whatisthis.domain.member.service.member.MemberAuthService;
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
}
