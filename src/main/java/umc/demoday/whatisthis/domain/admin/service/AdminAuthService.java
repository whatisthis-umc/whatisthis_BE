package umc.demoday.whatisthis.domain.admin.service;

import jakarta.servlet.http.HttpServletRequest;
import umc.demoday.whatisthis.domain.admin.dto.AdminLoginReqDTO;
import umc.demoday.whatisthis.domain.admin.dto.AdminLoginResDTO;

public interface AdminAuthService {
    AdminLoginResDTO login(AdminLoginReqDTO request);
    AdminLoginResDTO reissue(String refreshToken);
    void logout(Integer adminId, HttpServletRequest request);
}
