package umc.demoday.whatisthis.domain.member.service.email;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailAuthServiceImpl implements EmailAuthService {

    private final StringRedisTemplate redisTemplate;
    private final JavaMailSender mailSender;

    private static final long EXPIRE_MINUTES = 3;

    @Override
    public void sendAuthCode(String email) {
        String authCode = generateAuthCode();

        // Redis에 저장
        redisTemplate.opsForValue()
                .set(buildKey(email), authCode, EXPIRE_MINUTES, TimeUnit.MINUTES);

        try {
            // MimeMessage 생성
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            helper.setTo(email);
            helper.setSubject("[이게뭐예요] 이메일 인증 코드");
            helper.setText("인증코드: " + authCode, false);
            helper.setFrom(new InternetAddress("whatisthistest1111@gmail.com"));

            mailSender.send(mimeMessage);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("이메일 발송 실패", e);
        }
    }

    private String buildKey(String email) {
        return "EMAIL_AUTH:" + email;
    }

    private String generateAuthCode() {
        int code = (int) ((Math.random() * 900000) + 100000);
        return String.valueOf(code);
    }
}
