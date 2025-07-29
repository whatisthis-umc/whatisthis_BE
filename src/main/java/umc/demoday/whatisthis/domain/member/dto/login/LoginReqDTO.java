package umc.demoday.whatisthis.domain.member.dto.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginReqDTO {

    private String memberId;
    private String password;
}
