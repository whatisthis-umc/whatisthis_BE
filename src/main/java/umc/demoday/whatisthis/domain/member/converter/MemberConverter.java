package umc.demoday.whatisthis.domain.member.converter;

import org.springframework.security.crypto.password.PasswordEncoder;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.member.dto.member.MemberReqDTO;
import umc.demoday.whatisthis.domain.member.dto.member.MemberResDTO;

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
}
