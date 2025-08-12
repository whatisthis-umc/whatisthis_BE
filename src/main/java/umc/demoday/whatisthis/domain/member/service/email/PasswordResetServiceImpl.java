package umc.demoday.whatisthis.domain.member.service.email;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.member.dto.member.PasswordChangeReqDTO;
import umc.demoday.whatisthis.domain.member.repository.MemberRepository;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final MemberRepository memberRepository;
    private final StringRedisTemplate redisTemplate;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    private static final long EXPIRE_MINUTES = 3;

    @Override
    public void sendResetCode(String email) {

        if (!memberRepository.existsByEmail(email)) {
            throw new GeneralException(GeneralErrorCode.MEMBER_NOT_FOUND);
        }

        String authCode = generateAuthCode();

        redisTemplate.opsForValue()
                .set(buildKey(email), authCode, EXPIRE_MINUTES, TimeUnit.MINUTES);

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            helper.setTo(email);
            helper.setSubject("[이게뭐예요] 비밀번호 재설정 인증코드");
            helper.setText("아래 인증코드를 입력해 주세요.\n\n인증코드: " + authCode, false);
            helper.setFrom(new InternetAddress("whatisthistest1111@gmail.com"));

            mailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new RuntimeException("비밀번호 재설정 이메일 발송 실패", e);
        }
    }

    @Override
    public PasswordResetService.ResetToken verifyAndIssueResetToken(String memberId, String email, String code) {
        String saved = redisTemplate.opsForValue().get("PW_RESET:" + email);
        if (saved == null || !saved.trim().equals(code.trim()))
            throw new GeneralException(GeneralErrorCode.EMAIL_AUTH_CODE_MISMATCH);

        if (!memberRepository.existsByMemberIdAndEmail(memberId, email))
            throw new GeneralException(GeneralErrorCode.MEMBER_NOT_FOUND);

        String token = UUID.randomUUID().toString();
        Duration ttl = Duration.ofMinutes(10);
        redisTemplate.opsForValue().set("PW_RESET_OK:" + token, memberId + ":" + email, ttl);
        return new PasswordResetService.ResetToken(token, ttl);
    }

    @Override
    public void resetPassword(String resetToken, PasswordChangeReqDTO dto) {
        // 1) 토큰 → (memberId, email) 복원
        String data = redisTemplate.opsForValue().get("PW_RESET_OK:" + resetToken);
        if (data == null) throw new GeneralException(GeneralErrorCode.UNAUTHORIZED_401);

        String[] parts = data.split(":", 2);
        String memberId = parts[0];
        String email = parts[1];

        Member member = memberRepository.findByMemberIdAndEmail(memberId, email)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.MEMBER_NOT_FOUND));

        if (passwordEncoder.matches(dto.getNewPassword(), member.getPassword())) {
            throw new GeneralException(GeneralErrorCode.PASSWORD_SAME_AS_BEFORE);
        }

        // 변경 & 토큰 소멸
        member.changePassword(passwordEncoder.encode(dto.getNewPassword()));
        memberRepository.save(member);
        redisTemplate.delete("PW_RESET_OK:" + resetToken);
    }

    private String buildKey(String email) {
        return "PW_RESET:" + email;
    }

    private String generateAuthCode() {
        int code = (int) ((Math.random() * 900_000) + 100_000);
        return String.valueOf(code);
    }
}
