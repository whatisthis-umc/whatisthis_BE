package umc.demoday.whatisthis.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.demoday.whatisthis.domain.member.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByNickname(String nickname);

    boolean existsByMemberId(String memberId);
}
