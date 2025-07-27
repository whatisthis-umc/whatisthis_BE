package umc.demoday.whatisthis.domain.member.service.member;

import umc.demoday.whatisthis.domain.member.dto.member.FindIdReqDTO;
import umc.demoday.whatisthis.domain.member.dto.member.FindIdResDTO;

public interface MemberQueryService {
    FindIdResDTO findMemberIdByEmail(FindIdReqDTO request);
}
