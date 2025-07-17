package umc.demoday.whatisthis.service.member;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.member.dto.member.MemberReqDTO;
import umc.demoday.whatisthis.domain.member.dto.member.MemberResDTO;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;
import umc.demoday.whatisthis.domain.member.service.member.MemberCommandService;
import umc.demoday.whatisthis.domain.member.repository.MemberRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberCommandServiceImplTest {

    @Autowired
    MemberCommandService memberCommandService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    StringRedisTemplate redisTemplate;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }

    @Test
    @DisplayName("이메일 인증 안됐으면 회원가입 실패")
    void signup_fails_if_email_not_verified() {
        // given
        MemberReqDTO.JoinRequestDTO dto = createValidRequest();

        // Redis에 인증되지 않은 상태 (아무 것도 넣지 않음)

        // when & then
        GeneralException ex = assertThrows(
                GeneralException.class,
                () -> memberCommandService.signUp(dto)
        );

        assertEquals(GeneralErrorCode.EMAIL_NOT_VERIFIED.getCode(), ex.getCode().getCode());
    }

    @Test
    @DisplayName("아이디가 중복되면 회원가입 실패")
    void signup_fails_if_username_exists() {
        // given
        redisTemplate.opsForValue().set("EMAIL_AUTH_SUCCESS:test@email.com", "true");

        memberRepository.save(Member.builder()
                .memberId("testuser")
                .nickname("othernick")
                .email("someone@email.com")
                .password("encoded")
                .privacyAgreed(true)
                .serviceAgreed(true)
                .build()
        );

        MemberReqDTO.JoinRequestDTO dto = createValidRequest();

        // when & then
        GeneralException ex = assertThrows(
                GeneralException.class,
                () -> memberCommandService.signUp(dto)
        );

        assertEquals(GeneralErrorCode.ALREADY_EXIST_MEMBER_ID.getCode(), ex.getCode().getCode());
    }

    @Test
    @DisplayName("닉네임이 중복되면 회원가입 실패")
    void signup_fails_if_nickname_exists() {
        // given
        redisTemplate.opsForValue().set("EMAIL_AUTH_SUCCESS:test@email.com", "true");

        memberRepository.save(Member.builder()
                .memberId("otheruser")
                .nickname("nickname") // 중복 닉네임
                .email("someone@email.com")
                .password("encoded")
                .privacyAgreed(true)
                .serviceAgreed(true)
                .build()
        );

        MemberReqDTO.JoinRequestDTO dto = createValidRequest();

        // when & then
        GeneralException ex = assertThrows(
                GeneralException.class,
                () -> memberCommandService.signUp(dto)
        );

        assertEquals(GeneralErrorCode.ALREADY_EXIST_NICKNAME.getCode(), ex.getCode().getCode());
    }

    @Test
    @Rollback(false)
    @DisplayName("모든 조건이 충족되면 회원가입 성공 및 DB 저장")
    void signup_succeeds_if_all_valid() {
        // given
        redisTemplate.opsForValue().set("EMAIL_AUTH_SUCCESS:test@email.com", "true");

        MemberReqDTO.JoinRequestDTO dto = createValidRequest();

        // when
        MemberResDTO.JoinResponseDTO response = memberCommandService.signUp(dto);

        // then
        assertNotNull(response);
        assertEquals("nickname", response.nickname());

        boolean exists = memberRepository.existsByMemberId("testuser");
        assertTrue(exists);
    }

    private MemberReqDTO.JoinRequestDTO createValidRequest() {
        MemberReqDTO.JoinRequestDTO dto = new MemberReqDTO.JoinRequestDTO();
        dto.setEmail("test@email.com");
        dto.setEmailAuthCode("123456");
        dto.setUsername("testuser");
        dto.setPassword("password12");
        dto.setPasswordCheck("password12");
        dto.setNickname("nickname");
        dto.setServiceAgreed(true);
        dto.setPrivacyAgreed(true);
        return dto;
    }
}