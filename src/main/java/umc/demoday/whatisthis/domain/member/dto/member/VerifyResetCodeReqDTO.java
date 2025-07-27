package umc.demoday.whatisthis.domain.member.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class VerifyResetCodeReqDTO {

    @NotBlank
    private String emailLocal;

    @NotBlank
    private String emailDomain;

    @NotBlank
    private String code;

    @NotBlank
    private String memberId;

    @Schema(hidden = true)
    public String getFullEmail() {
        return emailLocal + "@" + emailDomain;
    }
}
