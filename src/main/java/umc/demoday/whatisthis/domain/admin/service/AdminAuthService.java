package umc.demoday.whatisthis.domain.admin.service;

import umc.demoday.whatisthis.domain.admin.dto.AdminLoginReqDTO;
import umc.demoday.whatisthis.domain.admin.dto.AdminLoginResDTO;

public interface AdminAuthService {
    AdminLoginResDTO login(AdminLoginReqDTO request);
}
