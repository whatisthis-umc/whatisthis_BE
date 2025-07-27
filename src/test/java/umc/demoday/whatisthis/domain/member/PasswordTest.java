package umc.demoday.whatisthis.domain.member;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest {

    @Test
    void generatePassword() {
        String raw = "1234";
        String encoded = new BCryptPasswordEncoder().encode(raw);
        System.out.println("Encoded password: " + encoded);
    }

}
