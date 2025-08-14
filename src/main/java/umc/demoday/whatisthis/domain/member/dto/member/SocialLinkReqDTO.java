package umc.demoday.whatisthis.domain.member.dto.member;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SocialLinkReqDTO {

    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    @NotBlank(message = "provider는 필수입니다.")
    private String provider;  // 예: "kakao", "google"

    @NotBlank(message = "providerId는 필수입니다.")
    private String providerId;
}
