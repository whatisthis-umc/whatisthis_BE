package umc.demoday.whatisthis.domain.member.dto.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SocialSignupReqDTO {

    @NotBlank
    private String nickname;

    @NotNull(message = "서비스 약관 동의는 필수입니다.")
    private Boolean serviceAgreed;

    @NotNull(message = "개인정보 처리방침 동의는 필수입니다.")
    private Boolean privacyAgreed;
}
