package umc.demoday.whatisthis.service.member;

import umc.demoday.whatisthis.dto.member.MemberReqDTO;
import umc.demoday.whatisthis.dto.member.MemberResDTO;

public interface MemberCommandService {
    MemberResDTO.JoinResponseDTO signUp(MemberReqDTO.JoinRequestDTO dto);
}
