package umc.demoday.whatisthis.domain.member.dto.member;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PasswordChangeReqDTO {

    @NotBlank
    private String email;

    @NotBlank
    private String newPassword;

    @NotBlank
    private String confirmPassword;
}
