package umc.demoday.whatisthis.global.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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

        // 1. Authorization 헤더에서 Bearer 토큰 추출
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);

            // 2. 토큰 유효성 검사
            if (jwtProvider.validateToken(token)) {
                Integer id = jwtProvider.getUserIdFromToken(token);
                String roleFromToken = jwtProvider.getRoleFromToken(token); // 토큰에서 역할 추출

                CustomUserDetails userDetails = null;

                if ("ROLE_USER".equals(roleFromToken)) {
                    Member member = memberRepository.findById(id).orElse(null);
                    if (member != null) {
                        userDetails = new CustomUserDetails(
                                member.getId(),
                                member.getMemberId(),
                                "",
                                "USER", // 역할 명시
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
                                "ADMIN", // 역할 명시
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
        }
        // 6. 다음 필터로 넘김
        filterChain.doFilter(request, response);
    }
}
