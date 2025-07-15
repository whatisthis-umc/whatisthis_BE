package umc.demoday.whatisthis.controller.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import umc.demoday.whatisthis.dto.member.MemberReqDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class MemberControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private MemberReqDTO.JoinRequestDTO createValidDto() {
        MemberReqDTO.JoinRequestDTO dto = new MemberReqDTO.JoinRequestDTO();
        dto.setEmail("test@email.com");
        dto.setEmailAuthCode("123456");
        dto.setUsername("username");
        dto.setPassword("password1234");
        dto.setPasswordCheck("password1234");
        dto.setNickname("nickname");
        dto.setServiceAgreed(true);
        dto.setPrivacyAgreed(true);
        return dto;
    }

    @Test
    @DisplayName("아이디에 특수문자가 들어가면 에러")
    void signup_fail_whenUsernameHasSpecialCharacters() throws Exception {
        var dto = createValidDto();
        dto.setUsername("user_name!");

        mockMvc.perform(post("/api/v0/members/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("특수문자는 사용할 수 없습니다."));
    }

    @Test
    @DisplayName("비밀번호가 10자 미만이면 에러")
    void signup_fail_whenPasswordTooShort() throws Exception {
        var dto = createValidDto();
        dto.setPassword("short1");
        dto.setPasswordCheck("short1");

        mockMvc.perform(post("/api/v0/members/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("비밀번호는 영문+숫자 포함 10자 이상이어야 합니다."));
    }

    @Test
    @DisplayName("비밀번호가 영문+숫자 조합이 아니면 에러")
    void signup_fail_whenPasswordNotAlphanumeric() throws Exception {
        var dto = createValidDto();
        dto.setPassword("abcdefghij");
        dto.setPasswordCheck("abcdefghij");

        mockMvc.perform(post("/api/v0/members/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("비밀번호는 영문+숫자 포함 10자 이상이어야 합니다."));
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않으면 에러")
    void signup_fail_whenPasswordsDoNotMatch() throws Exception {
        var dto = createValidDto();
        dto.setPassword("password1234");
        dto.setPasswordCheck("different1234");

        mockMvc.perform(post("/api/v0/members/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("비밀번호가 일치하지 않습니다."));
    }

    @Test
    @DisplayName("닉네임에 특수문자가 들어가면 에러")
    void signup_fail_whenNicknameHasSpecialCharacters() throws Exception {
        var dto = createValidDto();
        dto.setNickname("nick@name");

        mockMvc.perform(post("/api/v0/members/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("특수문자는 사용할 수 없습니다."));
    }
}
