package umc.demoday.whatisthis.domain.member.dto.member;

import lombok.*;

public class MyPageAccountDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyPageAccountResponseDTO {
        Integer id;
        String nickname;
        String email;
        String password;
        String profileImage;
    }

    @Getter @Setter
    public static class MyPageAccountRequestDTO {
        Integer id;
        String nickname;
        String email;
        String password;
        String profileImage;
    }


}
