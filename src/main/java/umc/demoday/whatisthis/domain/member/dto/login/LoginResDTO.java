package umc.demoday.whatisthis.domain.member.dto.login;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResDTO {

    private String accessToken;
    private String refreshToken;
}
