package umc.demoday.whatisthis.global.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import umc.demoday.whatisthis.domain.admin.Admin;
import umc.demoday.whatisthis.domain.admin.repository.AdminRepository;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.member.repository.MemberRepository;
import umc.demoday.whatisthis.global.CustomUserDetails;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    private final AdminRepository adminRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // CORS preflight 요청은 바로 통과
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        String token = resolveToken(request);

        try {
            if (token != null && jwtProvider.validateToken(token)) {
                Integer id = jwtProvider.getUserIdFromToken(token);
                String roleFromToken = jwtProvider.getRoleFromToken(token);

                CustomUserDetails userDetails = null;

                if ("ROLE_USER".equals(roleFromToken)) {
                    Member member = memberRepository.findById(id).orElse(null);
                    if (member != null) {
                        userDetails = new CustomUserDetails(
                                member.getId(),
                                member.getMemberId(),
                                "",
                                "ROLE_USER",
                                List.of(new SimpleGrantedAuthority("ROLE_USER"))
                        );
                    }
                } else if ("ROLE_ADMIN".equals(roleFromToken)) {
                    Admin admin = adminRepository.findById(id).orElse(null);
                    if (admin != null) {
                        userDetails = new CustomUserDetails(
                                admin.getId(),
                                admin.getAdminId(),
                                "",
                                "ROLE_ADMIN",
                                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
                        );
                    }
                }

                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            log.warn("JWT filter error: {}", e.getMessage(), e);
            // 토큰 문제로 전체 요청을 막지 않음 → 다음 체인으로
        }

        filterChain.doFilter(request, response);
    }

    /** Authorization: Bearer ... → 없으면 accessToken 쿠키에서 */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("accessToken".equals(c.getName())) {
                    return c.getValue();
                }
            }
        }
        return null;
    }
}