package umc.demoday.whatisthis.domain.member.dto.member;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class FindIdReqDTO {

    @NotBlank
    private String emailLocal;

    @NotBlank
    private String emailDomain;

    public String getFullEmail() {
        return emailLocal + "@" + emailDomain;
    }
}
