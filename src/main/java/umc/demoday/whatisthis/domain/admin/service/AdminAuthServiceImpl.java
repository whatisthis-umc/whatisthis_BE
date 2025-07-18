package umc.demoday.whatisthis.domain.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import umc.demoday.whatisthis.domain.admin.Admin;
import umc.demoday.whatisthis.domain.admin.dto.AdminLoginReqDTO;
import umc.demoday.whatisthis.domain.admin.dto.AdminLoginResDTO;
import umc.demoday.whatisthis.domain.admin.repository.AdminRepository;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;
import umc.demoday.whatisthis.global.security.JwtProvider;

@Service
@RequiredArgsConstructor
public class AdminAuthServiceImpl implements AdminAuthService {

    private final AdminRepository adminRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AdminLoginResDTO login(AdminLoginReqDTO request) {

        Admin admin = adminRepository.findByAdminId(request.getUsername())
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.ADMIN_ID_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new GeneralException(GeneralErrorCode.ADMIN_PASSWORD_MISMATCH);
        }

        String accessToken = jwtProvider.createAccessToken(admin.getId(), "ADMIN");
        String refreshToken = jwtProvider.createRefreshToken(admin.getId());

        return AdminLoginResDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
