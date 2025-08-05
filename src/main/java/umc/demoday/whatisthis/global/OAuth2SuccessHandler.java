package umc.demoday.whatisthis.global;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.member.repository.MemberRepository;
import umc.demoday.whatisthis.global.security.JwtProvider;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

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
        // 테스트용 추후 제거
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        if (member != null) {
            if (member.getProvider() == null) {
                // 기존 계정은 있지만 소셜 미연동 → 연동 여부 물어보도록 안내
                response.sendRedirect("http://localhost:5173/oauth-callback"
                        + "?conflict=true"
                        + "&email=" + URLEncoder.encode(email, StandardCharsets.UTF_8)
                        + "&provider=" + URLEncoder.encode(provider, StandardCharsets.UTF_8)
                        + "&providerId=" + URLEncoder.encode(providerId, StandardCharsets.UTF_8));
                return;
            }

            if (provider.equals(member.getProvider())) {
                // 소셜 연동이 된 기존 회원 → JWT 발급
                String accessToken = jwtProvider.createAccessToken(member.getId(), "USER");
                String refreshToken = jwtProvider.createRefreshToken(member.getId());

                response.sendRedirect("http://localhost:5173/oauth-callback"
                        + "?isNew=false"
                        + "&accessToken=" + URLEncoder.encode(accessToken, StandardCharsets.UTF_8)
                        + "&refreshToken=" + URLEncoder.encode(refreshToken, StandardCharsets.UTF_8));
                return;
            } else {
                // 이메일은 같지만 다른 provider에 연동되어 있음
                response.sendRedirect("http://localhost:5173/oauth-callback"
                        + "?error=conflict-provider"
                        + "&email=" + URLEncoder.encode(email, StandardCharsets.UTF_8));
                return;
            }
        }

        // 신규 유저 → 회원가입 유도
        response.sendRedirect("http://localhost:5173/oauth-callback"
                + "?isNew=true"
                + "&email=" + URLEncoder.encode(email, StandardCharsets.UTF_8)
                + "&provider=" + URLEncoder.encode(provider, StandardCharsets.UTF_8)
                + "&providerId=" + URLEncoder.encode(providerId, StandardCharsets.UTF_8));
    }
}

