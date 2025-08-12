package umc.demoday.whatisthis.domain.member.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import umc.demoday.whatisthis.domain.member.validation.annotation.PasswordMatches;

@PasswordMatches(first = "newPassword", second = "confirmPassword")
@Getter
public class PasswordChangeReqDTO {

    // 배포 서버에서는 필요없음 로컬 테스트용
    @Schema(hidden = true)
    private String resetToken;

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{10,}$",
            message = "비밀번호는 영문+숫자 포함 10자 이상이어야 합니다.")
    private String newPassword;

    @NotBlank
    private String confirmPassword;
}
