package umc.demoday.whatisthis.domain.member.service.member;

import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.member.dto.member.FindIdReqDTO;
import umc.demoday.whatisthis.domain.member.dto.member.FindIdResDTO;
import umc.demoday.whatisthis.global.CustomUserDetails;

public interface MemberQueryService {
    FindIdResDTO findMemberIdByEmail(FindIdReqDTO request);
    Member fetchedMember(Member member);
    Member findMemberByDetails(CustomUserDetails customUserDetails);
}
