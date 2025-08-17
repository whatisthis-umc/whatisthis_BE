package umc.demoday.whatisthis.domain.member.dto.member;

public class MemberResDTO {

    public record JoinResponseDTO(
            String nickname
    ) {}
    public static record IssuedTokens(String accessToken, String refreshToken) {}
}
