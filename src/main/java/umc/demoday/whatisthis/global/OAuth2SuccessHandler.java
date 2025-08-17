package umc.demoday.whatisthis.global;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
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
import java.time.Duration;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${app.front-base-url}")
    private String frontBase;   // 예: https://www.whatisthis.co.kr

    @Value("${app.cross-site:true}")
    private boolean crossSite;  // 프론트가 별도 도메인일 때 true (SameSite=None)

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        SocialUser su = mapSocialUser(authentication, oAuth2User);

        log.info("[OAuth2 success] provider={}, providerId={}, email={}", su.provider, su.providerId, su.email);

        // 이메일을 못 받은 경우 (동의 안했거나 제공자 세팅 문제)
        if (su.email == null || su.email.isBlank()) {
            log.warn("OAuth2 email is null. attrs={}", oAuth2User.getAttributes());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "이메일 정보를 받아올 수 없습니다.");
            return;
        }

        final String cb = frontBase + "/oauth-callback";

        Optional<Member> opt = memberRepository.findByEmail(su.email);
        if (opt.isPresent()) {
            Member member = opt.get();

            // 기존 계정인데 소셜 미연동
            if (member.getProvider() == null) {
                response.sendRedirect(cb
                        + "?conflict=true"
                        + "&email=" + enc(su.email)
                        + "&provider=" + enc(su.provider)
                        + "&providerId=" + enc(su.providerId));
                return;
            }

            // 동일 provider로 연동된 사용자 → 토큰 발급
            if (su.provider.equals(member.getProvider())) {
                String accessToken = jwtProvider.createAccessToken(member.getId(), "ROLE_USER");
                String refreshToken = jwtProvider.createRefreshToken(member.getId());

                // refreshToken upsert
                RefreshToken rt = refreshTokenRepository.findById(member.getId())
                        .map(existing -> {
                            existing.setToken(refreshToken);
                            return existing;
                        })
                        .orElse(new RefreshToken(member.getId(), refreshToken));
                refreshTokenRepository.save(rt);

                addHttpOnlyCookie(response, "accessToken", accessToken, Duration.ofHours(1));
                addHttpOnlyCookie(response, "refreshToken", refreshToken, Duration.ofDays(7));

                response.sendRedirect(cb + "?isNew=false");
                return;
            } else {
                // 이메일은 같지만 다른 소셜과 연동
                response.sendRedirect(cb + "?error=conflict-provider");
                return;
            }
        }

        // 신규 유저 → 프론트에서 회원가입 플로우로
        response.sendRedirect(cb
                + "?isNew=true"
                + "&email=" + enc(su.email)
                + "&provider=" + enc(su.provider)
                + "&providerId=" + enc(su.providerId)
                + "&name=" + enc(su.name));
    }

    /* ===== helpers ===== */

    private void addHttpOnlyCookie(HttpServletResponse response, String name, String value, Duration maxAge) {
        String sameSite = crossSite ? "None" : "Lax";
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(true)          // HTTPS 필수
                .sameSite(sameSite)    // cross-site면 None
                .path("/")
                .maxAge(maxAge)
                .domain(".whatisthis.co.kr")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private static String enc(String s) {
        return (s == null) ? "" : URLEncoder.encode(s, StandardCharsets.UTF_8);
    }

    /** 제공자별 Attribute를 우리 표준으로 통일 */
    private SocialUser mapSocialUser(Authentication authentication, OAuth2User user) {
        String provider = (authentication instanceof OAuth2AuthenticationToken o)
                ? o.getAuthorizedClientRegistrationId()
                : "unknown";

        Map<String, Object> attrs = user.getAttributes();
        String email = null, name = null, picture = null, providerId = null;

        switch (provider) {
            case "google" -> {
                email = (String) attrs.get("email");
                name = (String) attrs.getOrDefault("name", "");
                picture = (String) attrs.get("picture");
                providerId = (String) attrs.get("sub");
            }
            case "kakao" -> {
                Object id = attrs.get("id"); // Long
                providerId = (id == null) ? null : String.valueOf(id);

                Object accObj = attrs.get("kakao_account");
                if (accObj instanceof Map<?, ?> acc) {
                    Object e = acc.get("email");
                    if (e instanceof String s) email = s;

                    Object profObj = acc.get("profile");
                    if (profObj instanceof Map<?, ?> prof) {
                        Object nick = prof.get("nickname");
                        if (nick instanceof String s) name = s;
                        Object img = prof.get("profile_image_url");
                        if (img instanceof String s) picture = s;
                    }
                }
            }
            case "naver" -> {
                Object respObj = attrs.get("response");
                if (respObj instanceof Map<?, ?> resp) {
                    Object e = resp.get("email");
                    if (e instanceof String s) email = s;
                    Object n = resp.get("name");
                    if (n instanceof String s) name = s;
                    Object img = resp.get("profile_image");
                    if (img instanceof String s) picture = s;
                    Object pid = resp.get("id");
                    if (pid instanceof String s) providerId = s;
                }
            }
            default -> log.warn("Unknown provider or mapping not set: {}", provider);
        }
        return new SocialUser(provider, providerId, email, name, picture);
    }

    private record SocialUser(String provider, String providerId, String email, String name, String picture) {}
}