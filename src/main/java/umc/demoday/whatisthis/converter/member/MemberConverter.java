package umc.demoday.whatisthis.converter.member;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.dto.member.MemberReqDTO;
import umc.demoday.whatisthis.dto.member.MemberResDTO;

public class MemberConverter {

    // DTO -> Entity
    public static Member toMember(MemberReqDTO.JoinRequestDTO dto, PasswordEncoder encoder) {
        return Member.builder()
                .email(dto.email())
                .memberId(dto.username())
                .password(dto.password())
                .nickname(dto.nickname())
                .serviceAgreed(dto.serviceAgreed())
                .privacyAgreed(dto.privacyAgreed())
                .build();
    }

    // Entity -> DTO
    public static MemberResDTO.JoinResponseDTO toJoinResponseDTO(Member member) {
        return new MemberResDTO.JoinResponseDTO(
                member.getNickname()
        );
    }
}
