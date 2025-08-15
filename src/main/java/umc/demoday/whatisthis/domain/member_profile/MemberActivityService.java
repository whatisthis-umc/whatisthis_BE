package umc.demoday.whatisthis.domain.member_profile;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.demoday.whatisthis.domain.admin.Admin;
import umc.demoday.whatisthis.domain.admin.repository.AdminRepository;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.member.repository.MemberRepository;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.global.CustomUserDetails;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberActivityService {

    private final MemberProfileRepository memberProfileRepository;
    private final MemberRepository memberRepository;
    private final AdminRepository adminRepository;

    public void updateLastSeenPost(CustomUserDetails customUserDetails, Integer postId, Category category) {

        if(customUserDetails.getRole().equals("ROLE_USER")) {
            Integer memberId = customUserDetails.getId();
            // memberId로 MemberProfile을 찾습니다.
            MemberProfile memberProfile = memberProfileRepository.findByMember_Id(memberId)
                    .orElseGet(() -> {
                        // 프로필이 없다면(해당 유저의 첫 활동), 새로 생성합니다.
                        Member member = memberRepository.findById(memberId)
                                .orElseThrow(() -> new IllegalArgumentException("해당 Member를 찾을 수 없습니다. id=" + memberId));
                        return MemberProfile.builder().member(member).build();
                    });
            // 마지막으로 본 게시물 ID를 업데이트하고 저장합니다.
            if(category.toString().endsWith("_TIP"))
                memberProfile.updateLastSeenTipPost(postId);
            else
                memberProfile.updateLastSeenItemPost(postId);

            memberProfileRepository.save(memberProfile);
        }
        else throw new GeneralException(GeneralErrorCode.FORBIDDEN_403, "허용되지 않은 접근입니다.");
    }
}
