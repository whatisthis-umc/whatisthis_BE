package umc.demoday.whatisthis.domain.member.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.member.dto.member.FindIdReqDTO;
import umc.demoday.whatisthis.domain.member.dto.member.FindIdResDTO;
import umc.demoday.whatisthis.domain.member.repository.MemberRepository;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;

@Service
@RequiredArgsConstructor
public class MemberQueryServiceImpl implements MemberQueryService {

    private final MemberRepository memberRepository;

    @Override
    public FindIdResDTO findMemberIdByEmail(FindIdReqDTO request) {
        String email = request.getFullEmail();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.MEMBER_NOT_FOUND_BY_EMAIL));

        String maskedId = maskMemberId(member.getMemberId());
        return new FindIdResDTO(maskedId);
    }

    private static String maskMemberId(String id) {
        int length = id.length();
        int visibleCount;

        if (length <= 2) {
            return "*".repeat(length);
        } else if (length <= 4) {
            visibleCount = 1;
        } else if (length <= 6) {
            visibleCount = 2;
        } else if (length <= 8) {
            visibleCount = 3;
        } else {
            visibleCount = 4;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(id, 0, visibleCount);
        sb.append("*".repeat(length - visibleCount));

        return sb.toString();
    }

    @Override
    public Member fetchedMember(Member member) {

        return memberRepository.findByIdWithProfileImage(member.getId())
                .orElseThrow(()-> new GeneralException(GeneralErrorCode.MEMBER_NOT_FOUND));
    }
}
