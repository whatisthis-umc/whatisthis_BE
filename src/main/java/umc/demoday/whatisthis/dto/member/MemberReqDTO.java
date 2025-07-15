package umc.demoday.whatisthis.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import umc.demoday.whatisthis.validation.annotation.PasswordMatches;

@PasswordMatches
public class MemberReqDTO {

    @Getter
    @Setter
    @PasswordMatches
    public static class JoinRequestDTO {

        @Email
        @NotBlank
        private String email;

        @NotBlank
        private String emailAuthCode;

        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9가-힣]+$", message = "특수문자는 사용할 수 없습니다.")
        private String username;

        @NotBlank
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{10,}$",
                message = "비밀번호는 영문+숫자 포함 10자 이상이어야 합니다."
        )
        private String password;

        @NotBlank
        private String passwordCheck;

        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9가-힣]+$", message = "특수문자는 사용할 수 없습니다.")
        private String nickname;

        private Boolean serviceAgreed;
        private Boolean privacyAgreed;
    }
}
