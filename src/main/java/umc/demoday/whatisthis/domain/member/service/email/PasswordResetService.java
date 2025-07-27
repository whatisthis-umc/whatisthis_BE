package umc.demoday.whatisthis.domain.member.service.email;

import jakarta.validation.constraints.NotBlank;

public interface PasswordResetService {
    void sendResetCode(String memberId, String email);

    void verifyResetCode(String memberId, String code);
}
