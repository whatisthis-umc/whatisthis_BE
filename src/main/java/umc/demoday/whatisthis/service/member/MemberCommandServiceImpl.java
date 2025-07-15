package umc.demoday.whatisthis.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.demoday.whatisthis.converter.member.MemberConverter;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.dto.member.MemberReqDTO;
import umc.demoday.whatisthis.dto.member.MemberResDTO;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;
import umc.demoday.whatisthis.repository.member.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public MemberResDTO.JoinResponseDTO signUp(MemberReqDTO.JoinRequestDTO dto) {

        // 이메일 인증 코드 검사

        // 아이디 중복 검사
        if (memberRepository.existsByMemberId(dto.getUsername())) {
            throw new GeneralException(GeneralErrorCode.ALREADY_EXIST_MEMBER_ID);
        }

        // 닉네임 중복 검사
        if (memberRepository.existsByNickname(dto.getNickname())) {
            throw new GeneralException(GeneralErrorCode.ALREADY_EXIST_NICKNAME);
        }

        // DTO -> Entity
        Member newMember = MemberConverter.toMember(dto, passwordEncoder);

        // 회원 저장
        memberRepository.save(newMember);

        // Entity -> ResponseDTO
        return MemberConverter.toJoinResponseDTO(newMember);
    }
}
