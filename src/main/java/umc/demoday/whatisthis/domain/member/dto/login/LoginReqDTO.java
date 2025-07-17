package umc.demoday.whatisthis.domain.member.dto.login;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginReqDTO {

    private String username;
    private String password;
}
