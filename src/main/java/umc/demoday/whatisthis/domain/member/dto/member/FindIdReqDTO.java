package umc.demoday.whatisthis.domain.member.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class FindIdReqDTO {

    @NotBlank
    private String emailLocal;

    @NotBlank
    private String emailDomain;

    @Schema(hidden = true)
    public String getFullEmail() {
        return emailLocal + "@" + emailDomain;
    }
}
