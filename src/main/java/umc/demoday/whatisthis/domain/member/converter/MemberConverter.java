package umc.demoday.whatisthis.domain.member.converter;

import org.springframework.security.crypto.password.PasswordEncoder;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.member.dto.member.MemberReqDTO;
import umc.demoday.whatisthis.domain.member.dto.member.MemberResDTO;
import umc.demoday.whatisthis.domain.member.dto.member.MyPageAccountDTO;

public class MemberConverter {

    // DTO -> Entity
    public static Member toMember(MemberReqDTO.JoinRequestDTO dto, PasswordEncoder encoder) {
        return Member.builder()
                .email(dto.getEmail())
                .memberId(dto.getMemberId())
                .password(encoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .serviceAgreed(dto.getServiceAgreed())
                .privacyAgreed(dto.getPrivacyAgreed())
                .build();
    }

    // Entity -> DTO
    public static MemberResDTO.JoinResponseDTO toJoinResponseDTO(Member member) {
        return new MemberResDTO.JoinResponseDTO(
                member.getNickname()
        );
    }

    public static MyPageAccountDTO.MyPageAccountResponseDTO toMyPageAccountResponseDTO(Member member) {

        return MyPageAccountDTO.MyPageAccountResponseDTO.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .profileImage(member.getProfileImage() != null
                        ? member.getProfileImage().getImageUrl()
                        : null)
                .build();
    }

    public static MyPageAccountDTO.MyPageAccountModifyDTO toMyPageAccountModifyDTO(Member member) {

        return MyPageAccountDTO.MyPageAccountModifyDTO.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .profileImage(member.getProfileImage() != null
                ? member.getProfileImage().getImageUrl()
                : null)
                .build();
    }
}
