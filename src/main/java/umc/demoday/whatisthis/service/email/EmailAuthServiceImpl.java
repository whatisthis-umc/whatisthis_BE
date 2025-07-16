package umc.demoday.whatisthis.service.email;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailAuthServiceImpl implements EmailAuthService {

    private final StringRedisTemplate redisTemplate;
    private final JavaMailSender mailSender; // 네 프로젝트에 맞춰 Email 발송 컴포넌트 주입

    private static final long EXPIRE_MINUTES = 3;

    // 이메일 전송
    @Override
    public void sendAuthCode(String email) {
        String authCode = generateAuthCode();

        // Redis에 저장
        redisTemplate.opsForValue()
                .set(buildKey(email), authCode, EXPIRE_MINUTES, TimeUnit.MINUTES);

        // 메일 메시지 객체 생성
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[이게뭐예요] 이메일 인증 코드");
        message.setText("인증코드: " + authCode);

        // 이메일 전송
        mailSender.send(message);
    }

    // 이메일 인증 코드 검증
    @Override
    public boolean verifyAuthCode(String email, String authCode) {
        String key = buildKey(email);
        String savedCode = redisTemplate.opsForValue().get(key);

        return savedCode != null && savedCode.equals(authCode);
    }

    private String buildKey(String email) {
        return "EMAIL_AUTH:" + email;
    }

    private String generateAuthCode() {
        int code = (int) ((Math.random() * 900000) + 100000);
        return String.valueOf(code);
    }
}
