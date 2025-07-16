package umc.demoday.whatisthis.dto.email;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailAuthVerifyReqDTO {

    @Email(message = "이메일이 올바르지않습니다. 다시 확인해주세요.")
    @NotBlank
    private String email;

    @NotBlank
    private String authCode;
}
