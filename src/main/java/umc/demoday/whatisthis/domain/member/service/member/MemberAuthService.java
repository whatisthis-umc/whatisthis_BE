package umc.demoday.whatisthis.domain.member.service.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import umc.demoday.whatisthis.domain.member.dto.login.LoginReqDTO;
import umc.demoday.whatisthis.domain.member.dto.login.LoginResDTO;

public interface MemberAuthService {
    LoginResDTO login(LoginReqDTO request);
    LoginResDTO reissue(String refreshToken);
    void logout(Integer id, HttpServletRequest request, HttpServletResponse response);
}
