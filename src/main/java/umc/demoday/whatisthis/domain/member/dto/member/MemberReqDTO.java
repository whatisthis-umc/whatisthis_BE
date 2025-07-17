package umc.demoday.whatisthis.domain.member.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import umc.demoday.whatisthis.domain.member.validation.annotation.PasswordMatches;

public class MemberReqDTO {

    @Getter
    @Setter
    @PasswordMatches
    public static class JoinRequestDTO {

        @Email(message = "이메일이 올바르지않습니다. 다시 확인해주세요.")
        @NotBlank
        private String email;

        @NotBlank
        private String emailAuthCode;

        // 아이디 유효성 검사
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9가-힣]+$", message = "특수문자는 사용할 수 없습니다.")
        private String username;

        // 비밀번호 유효성 검사
        @NotBlank
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{10,}$",
                message = "비밀번호는 영문+숫자 포함 10자 이상이어야 합니다."
        )
        private String password;

        @NotBlank
        private String passwordCheck;

        // 닉네임 유효성 검사
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9가-힣]+$", message = "특수문자는 사용할 수 없습니다.")
        private String nickname;

        @NotNull(message = "서비스 이용 약관 동의 여부를 선택해주세요.")
        private Boolean serviceAgreed;

        @NotNull(message = "개인정보 접근 약관 동의 여부를 선택해주세요.")
        private Boolean privacyAgreed;
    }
}
