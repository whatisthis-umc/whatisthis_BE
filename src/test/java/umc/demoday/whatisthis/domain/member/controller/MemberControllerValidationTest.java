package umc.demoday.whatisthis.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import umc.demoday.whatisthis.domain.member.dto.member.MemberReqDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class MemberControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 성공 - Integration Test")
    void signup_success() throws Exception {
        MemberReqDTO.JoinRequestDTO requestDTO = createValidRequest();

        mockMvc.perform(
                        post("/api/v0/members/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result.nickname").value("nickname"));
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 형식이 올바르지 않음")
    void signup_fail_invalidEmailFormat() throws Exception {
        MemberReqDTO.JoinRequestDTO requestDTO = createValidRequest();
        requestDTO.setEmail("not-an-email");  // 올바르지 않은 이메일 형식

        mockMvc.perform(
                        post("/api/v0/members/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.result.email").value("이메일이 올바르지않습니다. 다시 확인해주세요."));
    }


    @Test
    @DisplayName("회원가입 실패 - username 특수문자 포함")
    void signup_fail_invalidUsername() throws Exception {
        MemberReqDTO.JoinRequestDTO requestDTO = createValidRequest();
        requestDTO.setUsername("user@name");

        mockMvc.perform(
                        post("/api/v0/members/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.result.username").value("특수문자는 사용할 수 없습니다."));
    }

    @Test
    @DisplayName("회원가입 실패 - nickname 특수문자 포함")
    void signup_fail_invalidNickname() throws Exception {
        MemberReqDTO.JoinRequestDTO requestDTO = createValidRequest();
        requestDTO.setNickname("nick@name");

        mockMvc.perform(
                        post("/api/v0/members/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.result.nickname").value("특수문자는 사용할 수 없습니다."));
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 10자 미만")
    void signup_fail_invalidPassword() throws Exception {
        MemberReqDTO.JoinRequestDTO requestDTO = createValidRequest();
        requestDTO.setPassword("short");
        requestDTO.setPasswordCheck("short");

        mockMvc.perform(
                        post("/api/v0/members/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.result.password").value("비밀번호는 영문+숫자 포함 10자 이상이어야 합니다."));
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호가 숫자만 있음")
    void signup_fail_passwordOnlyDigits() throws Exception {
        MemberReqDTO.JoinRequestDTO requestDTO = createValidRequest();
        requestDTO.setPassword("1234567890");
        requestDTO.setPasswordCheck("1234567890");

        mockMvc.perform(
                        post("/api/v0/members/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.result.password").value("비밀번호는 영문+숫자 포함 10자 이상이어야 합니다."));
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호가 영문만 있음")
    void signup_fail_passwordOnlyLetters() throws Exception {
        MemberReqDTO.JoinRequestDTO requestDTO = createValidRequest();
        requestDTO.setPassword("onlyletters");
        requestDTO.setPasswordCheck("onlyletters");

        mockMvc.perform(
                        post("/api/v0/members/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.result.password").value("비밀번호는 영문+숫자 포함 10자 이상이어야 합니다."));
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 불일치")
    void signup_fail_passwordsNotMatch() throws Exception {
        MemberReqDTO.JoinRequestDTO requestDTO = createValidRequest();
        requestDTO.setPassword("password1");
        requestDTO.setPasswordCheck("differentPassword");

        mockMvc.perform(
                        post("/api/v0/members/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.result.passwordCheck").value("비밀번호가 일치하지 않습니다."));
    }

    private MemberReqDTO.JoinRequestDTO createValidRequest() {
        MemberReqDTO.JoinRequestDTO requestDTO = new MemberReqDTO.JoinRequestDTO();
        requestDTO.setEmail("test@email.com");
        requestDTO.setEmailAuthCode("123456");
        requestDTO.setUsername("testuser");
        requestDTO.setPassword("password12");
        requestDTO.setPasswordCheck("password12");
        requestDTO.setNickname("nickname");
        requestDTO.setServiceAgreed(true);
        requestDTO.setPrivacyAgreed(true);
        return requestDTO;
    }
}
