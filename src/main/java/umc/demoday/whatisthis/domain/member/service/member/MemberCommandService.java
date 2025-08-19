package umc.demoday.whatisthis.domain.member.service.member;

import jakarta.validation.Valid;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.member.dto.member.MemberReqDTO;
import umc.demoday.whatisthis.domain.member.dto.member.MemberResDTO;
import umc.demoday.whatisthis.domain.member.dto.member.SocialLinkReqDTO;
import umc.demoday.whatisthis.domain.member.dto.member.SocialSignupReqDTO;
import umc.demoday.whatisthis.domain.member.dto.member.MyPageAccountDTO;

public interface MemberCommandService {
    MemberResDTO.JoinResponseDTO signUp(MemberReqDTO.JoinRequestDTO dto);
    void evaluateIsBest(Member member);
    Member updateMember(MyPageAccountDTO.MyPageAccountRequestDTO request, Member member, String url);
    MemberResDTO.IssuedTokens linkSocialByCookieToken(String linkToken);
    MemberResDTO.JoinResponseDTO signUpSocialByCookieToken(String signupToken, SocialSignupReqDTO request);
}
