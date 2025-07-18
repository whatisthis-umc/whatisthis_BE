package umc.demoday.whatisthis.domain.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.demoday.whatisthis.domain.admin.dto.AdminLoginReqDTO;
import umc.demoday.whatisthis.domain.admin.dto.AdminLoginResDTO;
import umc.demoday.whatisthis.domain.admin.service.AdminAuthService;
import umc.demoday.whatisthis.global.apiPayload.CustomResponse;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminAuthController {
    private final AdminAuthService adminAuthService;

    @PostMapping("/login")
    @Operation(summary = "관리자 로그인 API -by 이정준")
    public CustomResponse<AdminLoginResDTO> login(
            @RequestBody @Valid AdminLoginReqDTO request
    ) {
        AdminLoginResDTO response = adminAuthService.login(request);
        return CustomResponse.ok(response);
    }

    @PostMapping("/reissue")
    @Operation(summary = "관리자 accessToken 재발급 API -by 이정준", security = {@SecurityRequirement(name = "")})
    public CustomResponse<AdminLoginResDTO> reissue(@RequestHeader("Refresh-Token") String refreshToken) {
        AdminLoginResDTO response = adminAuthService.reissue(refreshToken);
        return CustomResponse.ok(response);
    }

}
