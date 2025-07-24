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
        if (id.length() <= 2) return "*".repeat(id.length());

        StringBuilder sb = new StringBuilder();
        sb.append(id.charAt(0)); // 첫 글자는 그대로 노출

        for (int i = 1; i < id.length() - 2; i++) {
            sb.append("*"); // 중간 글자들을 *로 가림
        }

        sb.append(id.substring(id.length() - 2)); // 마지막 2글자는 그대로 노출

        return sb.toString();
    }
}
