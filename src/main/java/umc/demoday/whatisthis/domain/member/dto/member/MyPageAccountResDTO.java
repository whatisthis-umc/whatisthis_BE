package umc.demoday.whatisthis.domain.member.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MyPageAccountResDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyPageAccountDTO {
        Integer id;
        String nickname;
        String email;
        String password;
        String profileImage;
    }
}
