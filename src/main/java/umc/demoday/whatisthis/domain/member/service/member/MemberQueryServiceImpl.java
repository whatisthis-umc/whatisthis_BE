package umc.demoday.whatisthis.domain.member.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.member.dto.member.FindIdReqDTO;
import umc.demoday.whatisthis.domain.member.dto.member.FindIdResDTO;
import umc.demoday.whatisthis.domain.member.repository.MemberRepository;
import umc.demoday.whatisthis.global.CustomUserDetails;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberQueryServiceImpl implements MemberQueryService {

    private final MemberRepository memberRepository;

    @Override
    public FindIdResDTO findMemberIdByEmail(FindIdReqDTO request) {
        String raw = request.getFullEmail();
        if (raw == null) {
            throw new GeneralException(GeneralErrorCode.MEMBER_NOT_FOUND_BY_EMAIL);
        }
        String email = raw.trim().toLowerCase();

        // 중복 대응: 다건 조회
        List<Member> list = memberRepository.findAllByEmail(email);
        if (list.isEmpty()) {
            throw new GeneralException(GeneralErrorCode.MEMBER_NOT_FOUND_BY_EMAIL);
        }

        // 로컬 계정(= memberId != null && not blank) 우선 반환
        for (Member m : list) {
            String mid = m.getMemberId();
            if (mid != null && !mid.isBlank()) {
                String maskedId = maskMemberId(mid); // 기존 구현 그대로 사용
                return new FindIdResDTO(maskedId);
            }
        }

        // 로컬이 하나도 없다면 소셜 전용
        throw new GeneralException(GeneralErrorCode.EMAIL_SOCIAL_ONLY);
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

    @Override
    public Member findMemberByDetails(CustomUserDetails customUserDetails) {

        if (!customUserDetails.getRole().equals("ROLE_USER")) {
            throw new GeneralException(GeneralErrorCode.INVALID_ROLE);
        }

        return memberRepository.findById(customUserDetails.getId())
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.MEMBER_NOT_FOUND));
    }
}