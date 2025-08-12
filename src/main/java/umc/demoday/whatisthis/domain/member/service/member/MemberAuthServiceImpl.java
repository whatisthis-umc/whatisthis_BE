package umc.demoday.whatisthis.domain.member.service.member;

import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class MemberAuthServiceImpl implements MemberAuthService {

    private final AuthenticationManager authenticationManager;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

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

}
