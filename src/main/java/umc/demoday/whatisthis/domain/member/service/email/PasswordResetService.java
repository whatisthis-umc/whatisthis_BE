package umc.demoday.whatisthis.domain.member.service.email;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import umc.demoday.whatisthis.domain.member.dto.member.PasswordChangeReqDTO;

import java.time.Duration;

public interface PasswordResetService {
    ResetToken verifyAndIssueResetToken(String memberId, String email, String code);
    void resetPassword(String resetToken, PasswordChangeReqDTO dto);
    void sendResetCode(String email);
    record ResetToken(String value, Duration ttl) {}
}


