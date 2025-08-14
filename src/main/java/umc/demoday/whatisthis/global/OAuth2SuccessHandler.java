package umc.demoday.whatisthis.global;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.member.repository.MemberRepository;
import umc.demoday.whatisthis.domain.refresh_token.RefreshToken;
import umc.demoday.whatisthis.domain.refresh_token.repository.RefreshTokenRepository;
import umc.demoday.whatisthis.global.security.JwtProvider;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${app.front-base-url}")
    private String frontBase;

    @Value("${app.cross-site}")
    private boolean crossSite; // vercel은 true, 같은 사이트(whatisthis.co.kr)로 바뀌면 false로

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = (String) oAuth2User.getAttribute("email");
        String provider = (String) oAuth2User.getAttribute("provider");
        String providerId = (String) oAuth2User.getAttribute("providerId");

        if (email == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "이메일 정보를 받아올 수 없습니다.");
            return;
        }

        Member member = memberRepository.findByEmail(email).orElse(null);

        final String cb = frontBase + "/oauth-callback"; // 프론트 콜백 고정

        if (member != null) {
            if (member.getProvider() == null) {
                // 기존 계정은 있지만 소셜 미연동 → 연동 여부 물어보도록 안내
                response.sendRedirect(cb
                        + "?conflict=true"
                        + "&email=" + URLEncoder.encode(email, StandardCharsets.UTF_8)
                        + "&provider=" + URLEncoder.encode(provider, StandardCharsets.UTF_8)
                        + "&providerId=" + URLEncoder.encode(providerId, StandardCharsets.UTF_8));
                return;
            }

            if (provider.equals(member.getProvider())) {
                // 소셜 연동이 된 기존 회원 → JWT 발급
                String accessToken = jwtProvider.createAccessToken(member.getId(), "ROLE_USER");
                String refreshToken = jwtProvider.createRefreshToken(member.getId());

                refreshTokenRepository.save(new RefreshToken(member.getId(), refreshToken));

                addHttpOnlyCookie(response, "accessToken", accessToken, 60 * 60, crossSite); // 1시간
                addHttpOnlyCookie(response, "refreshToken", refreshToken, 60 * 60 * 24 * 7, crossSite); // 7일

                response.sendRedirect(cb + "?isNew=false");
                return;
            } else {
                // 이메일은 같지만 다른 provider에 연동되어 있음
                response.sendRedirect(cb + "?error=conflict-provider");
                return;
            }
        }

        // 신규 유저 → 회원가입 유도
        response.sendRedirect(cb
                + "?isNew=true"
                + "&email=" + URLEncoder.encode(email, StandardCharsets.UTF_8)
                + "&provider=" + URLEncoder.encode(provider, StandardCharsets.UTF_8)
                + "&providerId=" + URLEncoder.encode(providerId, StandardCharsets.UTF_8));
    }

    private void addHttpOnlyCookie(HttpServletResponse response, String name, String value, int maxAge, boolean crossSite) {
        String sameSite = crossSite ? "None" : "Lax";

        ResponseCookie cookie = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(true)                 // 배포(HTTPS)에서 필수. SameSite=None이면 Secure 필수
                .path("/")
                .maxAge(maxAge)
                .sameSite(sameSite)           // "None" or "Lax"
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}

