package umc.demoday.whatisthis.domain.member.dto.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SocialSignupReqDTO {

    @NotBlank
    private String email;

    @NotBlank
    private String nickname;

    @NotBlank
    private String provider;     // "naver", "kakao", "google"

    @NotBlank
    private String providerId;

    @NotNull(message = "서비스 약관 동의는 필수입니다.")
    private Boolean serviceAgreed;

    @NotNull(message = "개인정보 처리방침 동의는 필수입니다.")
    private Boolean privacyAgreed;
}
