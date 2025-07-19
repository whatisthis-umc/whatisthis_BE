package umc.demoday.whatisthis.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminLoginResDTO {
    private String accessToken;
    private String refreshToken;
}
