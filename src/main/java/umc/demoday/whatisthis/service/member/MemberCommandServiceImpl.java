package umc.demoday.whatisthis.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import umc.demoday.whatisthis.converter.member.MemberConverter;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.dto.member.MemberReqDTO;
import umc.demoday.whatisthis.dto.member.MemberResDTO;
import umc.demoday.whatisthis.repository.member.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public MemberResDTO.JoinResponseDTO signUp(MemberReqDTO.JoinRequestDTO dto) {

        // ?. 아이디 중복 검사
        if (memberRepository.existsByMemberId(dto.username())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        // ?. 비밀번호 일치 검사
        if (!dto.password().equals(dto.passwordCheck())) {
            throw new IllegalArgumentException("비밀번호가 일치하지않습니다. 다시 확인해주세요.");
        }

        // ?. 닉네임 중복 검사
        if (memberRepository.existsByNickname(dto.nickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        // DTO -> Entity
        Member newMember = MemberConverter.toMember(dto, passwordEncoder);

        // 회원 저장
        memberRepository.save(newMember);

        // Entity -> ResponseDTO
        return MemberConverter.toJoinResponseDTO(newMember);
    }
}
