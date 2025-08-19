package umc.demoday.whatisthis.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.demoday.whatisthis.domain.refresh_token.repository.RefreshTokenRepository;
import umc.demoday.whatisthis.global.apiPayload.CustomResponse;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;
import umc.demoday.whatisthis.global.security.JwtProvider;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthBootstrapController {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/bootstrap")
    public CustomResponse<Map<String, String>> bootstrap(
            @CookieValue(value = "refreshToken", required = false) String refreshToken) {

        if (refreshToken == null || refreshToken.isBlank()) {
            throw new GeneralException(GeneralErrorCode.UNAUTHORIZED_401);
        }

        // 1) 리프레시 서명/만료 검증 → memberId 추출
        Integer memberId = jwtProvider.validateAndGetMemberIdFromRefresh(refreshToken);

        // 2) DB 최신 리프레시와 일치 확인 (탈취·중복 방지)
        var rtOpt = refreshTokenRepository.findById(memberId);
        if (rtOpt.isEmpty() || !rtOpt.get().getToken().equals(refreshToken)) {
            throw new GeneralException(GeneralErrorCode.UNAUTHORIZED_401);
        }

        // 3) 새 액세스 토큰 발급 (필요시 리프레시 로테이션도 가능)
        String accessToken = jwtProvider.createAccessToken(memberId, "ROLE_USER");

        return CustomResponse.ok(Map.of("accessToken", accessToken));
    }
}
