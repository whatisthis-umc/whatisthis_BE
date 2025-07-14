package umc.demoday.whatisthis.dto.member;

public class MemberReqDTO {

    public record JoinRequestDTO(
        String email,
        String emailAuthCode, // 이메일 인증 코드
        String username, // 사용자가 입력한 아이디
        String password,
        String nickname,
        Boolean serviceAgreed,
        Boolean privacyAgreed
    ) {}

}
