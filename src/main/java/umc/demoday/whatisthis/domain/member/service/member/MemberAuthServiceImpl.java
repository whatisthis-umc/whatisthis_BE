package umc.demoday.whatisthis.domain.member.service.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.member.dto.login.LoginReqDTO;
import umc.demoday.whatisthis.domain.member.dto.login.LoginResDTO;
import umc.demoday.whatisthis.domain.member.repository.MemberRepository;
import umc.demoday.whatisthis.domain.refresh_token.RefreshToken;
import umc.demoday.whatisthis.domain.refresh_token.repository.RefreshTokenRepository;
import umc.demoday.whatisthis.global.CustomUserDetails;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;
import umc.demoday.whatisthis.global.security.JwtProvider;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MemberAuthServiceImpl implements MemberAuthService {

    private final AuthenticationManager authenticationManager;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final StringRedisTemplate redisTemplate;

    @Override
    public LoginResDTO login(LoginReqDTO request) {

        // 스프링 표준 인증
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getMemberId(), request.getPassword())
        );

        // 인증 성공 -> UserDetails 획득
        CustomUserDetails principal = (CustomUserDetails) auth.getPrincipal();

        Member member = memberRepository.findByMemberId(request.getMemberId())
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.MEMBER_NOT_FOUND));

        // 로그인 시점 기록
        member.setLastLoginAt(LocalDateTime.now());
        memberRepository.save(member);

        String role = principal.getRole();
        String accessToken = jwtProvider.createAccessToken(principal.getId(), role);

        // DB에 기존 refreshToken이 있는지 확인
        String refreshToken;
        RefreshToken savedToken = refreshTokenRepository.findById(principal.getId()).orElse(null);

        if (savedToken != null && jwtProvider.validateToken(savedToken.getToken())) {
            refreshToken = savedToken.getToken(); // 유효하면 재사용
        } else {
            refreshToken = jwtProvider.createRefreshToken(principal.getId());
            refreshTokenRepository.save(new RefreshToken(principal.getId(), refreshToken));
        }

        return new LoginResDTO(accessToken, refreshToken);
    }

    @Override
    public LoginResDTO reissue(String refreshToken) {

        // 1. 토큰 유효성 검사
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new GeneralException(GeneralErrorCode.UNAUTHORIZED_401); // 만료 등
        }

        // 2. 사용자 ID 추출
        Integer memberId = jwtProvider.getUserIdFromToken(refreshToken);

        // 3. DB에 저장된 refreshToken과 비교
        RefreshToken saved = refreshTokenRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.UNAUTHORIZED_401));

        if (!saved.getToken().equals(refreshToken)) {
            throw new GeneralException(GeneralErrorCode.UNAUTHORIZED_401); // 위조된 토큰
        }

        // 4. 새 AccessToken 생성
        String newAccessToken = jwtProvider.createAccessToken(memberId, "ROLE_USER");

        // (선택) RefreshToken도 새로 발급하여 갱신
        String newRefreshToken = jwtProvider.createRefreshToken(memberId);
        saved.updateToken(newRefreshToken);
        refreshTokenRepository.save(saved);

        return new LoginResDTO(newAccessToken, newRefreshToken);
    }

    // access 토큰 블랙리스트 (남은 만료시간만큼)
    @Override
    public void logout(Integer memberId, HttpServletRequest request, HttpServletResponse response) {
        String bearerToken = request.getHeader("Authorization");
        String accessToken = (bearerToken != null && bearerToken.startsWith("Bearer ")) ? bearerToken.substring(7) : null;

        refreshTokenRepository.deleteById(memberId);

        if (accessToken != null && jwtProvider.validateToken(accessToken)) {
            String jti = jwtProvider.getJti(accessToken);
            long ttlSec = Math.max(1,
                    (jwtProvider.getExpiration(accessToken).getTime() - System.currentTimeMillis()) / 1000);
            redisTemplate.opsForValue().set("bl:" + jti, "1", ttlSec, TimeUnit.SECONDS);
        }

        deleteCookie(response, "accessToken");
        deleteCookie(response, "refreshToken");
        deleteCookie(response, "linkToken");
    }

    private void deleteCookie(HttpServletResponse response, String name) {
        ResponseCookie del = ResponseCookie.from(name, "")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")             // FE와 BE가 크로스사이트면 None
                .domain(".whatisthis.co.kr")
                .path("/")
                .maxAge(0)                    // 즉시 만료
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, del.toString());
    }
}
