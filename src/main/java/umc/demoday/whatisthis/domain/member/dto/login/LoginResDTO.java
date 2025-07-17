package umc.demoday.whatisthis.domain.member.dto.login;

import lombok.Getter;

@Getter
public class LoginResDTO {

    private String accessToken;
    private String refreshToken;
}
