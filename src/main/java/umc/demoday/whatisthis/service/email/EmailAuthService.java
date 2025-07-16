package umc.demoday.whatisthis.service.email;

public interface EmailAuthService {

    void sendAuthCode(String email);

    boolean verifyAuthCode(String email, String authCode);
}
