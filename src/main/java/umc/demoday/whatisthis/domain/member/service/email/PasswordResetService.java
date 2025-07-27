package umc.demoday.whatisthis.domain.member.service.email;

public interface PasswordResetService {
    void sendResetCode(String memberId, String email);
}
