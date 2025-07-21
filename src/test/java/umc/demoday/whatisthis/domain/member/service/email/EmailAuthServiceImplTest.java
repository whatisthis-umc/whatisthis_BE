package umc.demoday.whatisthis.domain.member.service.email;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import umc.demoday.whatisthis.domain.member.service.email.EmailAuthService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class EmailAuthServiceImplTest {

//    @Autowired
//    EmailAuthService emailAuthService;
//    @Autowired
//    StringRedisTemplate redisTemplate;
//
//    private static final String TEST_EMAIL = "test@email.com";
//
//    @AfterEach
//    void tearDown() {
//        redisTemplate.getConnectionFactory().getConnection().flushDb();
//    }
//
//    @Test
//    @DisplayName("인증 코드를 전송하면 Redis에 저장된다")
//    void sendAuthCode_savesCodeInRedis() {
//        // when
//        emailAuthService.sendAuthCode(TEST_EMAIL);
//
//        // then
//        String savedCode = redisTemplate.opsForValue().get("EMAIL_AUTH:" + TEST_EMAIL);
//        assertThat(savedCode).isNotNull();
//        System.out.println("저장된 인증코드: " + savedCode);
//    }
//
//    @Test
//    @DisplayName("저장된 인증코드와 일치하면 인증 성공")
//    void verifyAuthCode_returnsTrue_whenCodeMatches() {
//        // given
//        String authCode = "123456";
//        redisTemplate.opsForValue()
//                .set("EMAIL_AUTH:" + TEST_EMAIL, authCode);
//
//        // when
//        boolean result = emailAuthService.verifyAuthCode(TEST_EMAIL, authCode);
//
//        // then
//        assertThat(result).isTrue();
//    }
//
//    @Test
//    @DisplayName("저장된 인증코드와 불일치하면 인증 실패")
//    void verifyAuthCode_returnsFalse_whenCodeDoesNotMatch() {
//        // given
//        redisTemplate.opsForValue()
//                .set("EMAIL_AUTH:" + TEST_EMAIL, "654321");
//
//        // when
//        boolean result = emailAuthService.verifyAuthCode(TEST_EMAIL, "999999");
//
//        // then
//        assertThat(result).isFalse();
//    }
}