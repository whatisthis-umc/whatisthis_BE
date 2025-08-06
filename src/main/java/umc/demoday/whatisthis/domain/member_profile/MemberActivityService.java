package umc.demoday.whatisthis.domain.member_profile;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.member.repository.MemberRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberActivityService {

    private final MemberProfileRepository memberProfileRepository;
    private final MemberRepository memberRepository;

    public void updateLastSeenPost(Integer memberId, Integer postId) {
        // memberId로 MemberProfile을 찾습니다.
        MemberProfile memberProfile = memberProfileRepository.findByMember_Id(memberId)
                .orElseGet(() -> {
                    // 프로필이 없다면(해당 유저의 첫 활동), 새로 생성합니다.
                    Member member = memberRepository.findById(memberId)
                            .orElseThrow(() -> new IllegalArgumentException("해당 Member를 찾을 수 없습니다. id=" + memberId));
                    return MemberProfile.builder().member(member).build();
                });

        // 마지막으로 본 게시물 ID를 업데이트하고 저장합니다.
        memberProfile.updateLastSeenPost(postId);
        memberProfileRepository.save(memberProfile);
    }
}
