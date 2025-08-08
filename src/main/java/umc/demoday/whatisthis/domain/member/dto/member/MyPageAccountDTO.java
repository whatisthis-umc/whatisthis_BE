package umc.demoday.whatisthis.domain.member.dto.member;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
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
        String profileImage;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyPageAccountModifyDTO {
        Integer id;
        String nickname;
        String email;
        String profileImage;
    }

    @Getter @Setter
    public static class MyPageAccountRequestDTO {
        @NotNull(message = "식별값 id는 필수입니다.")
        Integer id;

        @Size(min = 1, max = 20, message = "닉네임은 20자 이내로 작성해야합니다.")
        @Pattern(regexp = "^[a-zA-Z0-9가-힣]+$", message = "닉네임은 영문, 한글, 숫자만 사용할 수 있습니다.")
        String nickname;

        @Size(min = 10, max = 100, message = "비밀번호는 10자 이상이어야합니다.")
        @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "비밀번호는 영문과 숫자만 사용할 수 있습니다.")
        String password;

        @NotNull(message = "프로필사진 수정 여부는 필수입니다.")
        Boolean modifyProfileImage;

        String profileImage;
    }


}
