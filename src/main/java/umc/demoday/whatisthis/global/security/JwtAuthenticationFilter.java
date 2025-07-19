package umc.demoday.whatisthis.global.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.member.repository.MemberRepository;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. Authorization 헤더에서 Bearer 토큰 추출
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);

            // 2. 토큰 유효성 검사
            if (jwtProvider.validateToken(token)) {
                Integer memberId = jwtProvider.getUserIdFromToken(token);

                // 3. DB에서 사용자 조회 (기본 정보만 필요하면 생략 가능)
                Member member = memberRepository.findById(memberId)
                        .orElse(null);

                if (member != null) {
                    // 4. 인증 객체 생성
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(member, null, null);

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 5. SecurityContext에 등록
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        // 6. 다음 필터로 넘김
        filterChain.doFilter(request, response);
    }
}
