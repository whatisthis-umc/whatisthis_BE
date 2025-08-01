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
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // 카카오에서 가져온 이메일
        Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
        String email = (String) kakaoAccount.get("email");

        // 여기서 DB 조회: 기존 유저 확인
        Member member = memberRepository.findByEmail(email).orElse(null);

        if (member != null) {
            // 기존 회원 → JWT 발급
            Integer memberId = member.getId();
            String accessToken = jwtProvider.createAccessToken(memberId, "USER");
            String refreshToken = jwtProvider.createRefreshToken(memberId);

            // 프론트로 JWT 토큰 리다이렉트
            response.sendRedirect("http://localhost:5173/oauth-success?accessToken=" + accessToken + "&refreshToken=" + refreshToken);
        } else {
            // 신규 유저 → 닉네임/약관 입력 페이지로 리다이렉트
            // 프론트는 email 기반으로 회원가입을 진행할 수 있음
            response.sendRedirect("http://localhost:5173/oauth-signup?email=" + email);
        }
    }
}
