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

        // 이메일 인증 코드 검사

        // 아이디 유효성 검사
        if (!dto.username().matches("^[a-zA-Z0-9가-힣]+$")) {
            throw new IllegalArgumentException("특수문자는 사용할 수 없습니다.");
        }

        // 아이디 중복 검사
        if (memberRepository.existsByMemberId(dto.username())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        // 비밀번호 유효성 검사
        if (!dto.password().matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{10,}$")) {
            throw new IllegalArgumentException("비밀번호는 영문+숫자 포함 10자 이상이어야 합니다.");
        }

        // 비밀번호 일치 검사
        if (!dto.password().equals(dto.passwordCheck())) {
            throw new IllegalArgumentException("비밀번호가 일치하지않습니다. 다시 확인해주세요.");
        }

        // 닉네임 유효성 검사
        if (!dto.nickname().matches("^[a-zA-Z0-9가-힣]+$")) {
            throw new IllegalArgumentException("특수문자는 사용할 수 없습니다.");
        }

        // 닉네임 중복 검사
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
