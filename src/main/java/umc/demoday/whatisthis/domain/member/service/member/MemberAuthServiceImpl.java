package umc.demoday.whatisthis.domain.member.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.member.dto.login.LoginReqDTO;
import umc.demoday.whatisthis.domain.member.dto.login.LoginResDTO;
import umc.demoday.whatisthis.domain.member.repository.MemberRepository;
import umc.demoday.whatisthis.domain.refresh_token.RefreshToken;
import umc.demoday.whatisthis.domain.refresh_token.repository.RefreshTokenRepository;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;
import umc.demoday.whatisthis.global.security.JwtProvider;

@Service
@RequiredArgsConstructor
public class MemberAuthServiceImpl implements MemberAuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public LoginResDTO login(LoginReqDTO request) {

        Member member = memberRepository.findByMemberId(request.getUsername())
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new GeneralException(GeneralErrorCode.INVALID_PASSWORD);
        }

        String accessToken = jwtProvider.createAccessToken(member.getId(), "ROLE_USER");
        String refreshToken = jwtProvider.createRefreshToken(member.getId());

        refreshTokenRepository.save(new RefreshToken(member.getId(), refreshToken));

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
