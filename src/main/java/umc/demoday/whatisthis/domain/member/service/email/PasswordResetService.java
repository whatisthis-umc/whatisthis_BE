package umc.demoday.whatisthis.domain.member.service.email;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import umc.demoday.whatisthis.domain.member.dto.member.PasswordChangeReqDTO;

public interface PasswordResetService {
    void sendResetCode(String memberId, String email);

    void verifyResetCode(String memberId, String code);

    void resetPassword(PasswordChangeReqDTO dto);
}
