package umc.demoday.whatisthis.domain.admin.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import umc.demoday.whatisthis.domain.admin.Admin;
import umc.demoday.whatisthis.domain.admin.dto.AdminLoginReqDTO;
import umc.demoday.whatisthis.domain.admin.dto.AdminLoginResDTO;
import umc.demoday.whatisthis.domain.admin.repository.AdminRepository;
import umc.demoday.whatisthis.domain.refresh_token.AdminRefreshToken;
import umc.demoday.whatisthis.domain.refresh_token.repository.AdminRefreshTokenRepository;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;
import umc.demoday.whatisthis.global.security.JwtProvider;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AdminAuthServiceImpl implements AdminAuthService {

    private final AdminRepository adminRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AdminRefreshTokenRepository adminRefreshTokenRepository;
    private final StringRedisTemplate redisTemplate;

    @Override
    public AdminLoginResDTO login(AdminLoginReqDTO request) {

        Admin admin = adminRepository.findByAdminId(request.getUsername())
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.ADMIN_ID_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new GeneralException(GeneralErrorCode.ADMIN_PASSWORD_MISMATCH);
        }

        String accessToken = jwtProvider.createAccessToken(admin.getId(), "ROLE_ADMIN");

        String refreshToken;
        AdminRefreshToken savedToken = adminRefreshTokenRepository.findById(admin.getId()).orElse(null);

        if (savedToken != null && jwtProvider.validateToken(savedToken.getToken())) {
            refreshToken = savedToken.getToken();
        } else {
            refreshToken = jwtProvider.createRefreshToken(admin.getId());
            adminRefreshTokenRepository.save(new AdminRefreshToken(admin.getId(), refreshToken));
        }

        return new AdminLoginResDTO(accessToken, refreshToken);
    }

    @Override
    public AdminLoginResDTO reissue(String refreshToken) {

        // 1. 토큰 유효성 검사
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new GeneralException(GeneralErrorCode.UNAUTHORIZED_401); // 만료 등
        }

        // 2. 사용자 ID 추출
        Integer adminId = jwtProvider.getUserIdFromToken(refreshToken);

        // 3. DB에 저장된 refreshToken과 비교
        AdminRefreshToken saved = adminRefreshTokenRepository.findById(adminId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.UNAUTHORIZED_401));

        if (!saved.getToken().equals(refreshToken)) {
            throw new GeneralException(GeneralErrorCode.UNAUTHORIZED_401); // 위조된 토큰
        }

        // 4. 새 AccessToken 생성
        String newAccessToken = jwtProvider.createAccessToken(adminId, "ROLE_ADMIN");

        // (선택) RefreshToken도 새로 발급하여 갱신
        String newRefreshToken = jwtProvider.createRefreshToken(adminId);
        saved.updateToken(newRefreshToken);
        adminRefreshTokenRepository.save(saved);

        return new AdminLoginResDTO(newAccessToken, newRefreshToken);
    }

    @Override
    public void logout(Integer adminId, HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        String accessToken = null;
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            accessToken = bearerToken.substring(7);
        }

        adminRefreshTokenRepository.deleteById(adminId);
        if (accessToken != null && jwtProvider.validateToken(accessToken)) {
            String jti = jwtProvider.getJti(accessToken);
            long ttlSec = Math.max(1,
                    (jwtProvider.getExpiration(accessToken).getTime() - System.currentTimeMillis()) / 1000);
            redisTemplate.opsForValue().set("bl:" + jti, "1", ttlSec, TimeUnit.SECONDS);
        }
    }
}
