package umc.demoday.whatisthis.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.demoday.whatisthis.domain.member.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    boolean existsByNickname(String nickname);

    boolean existsByMemberId(String memberId);

    boolean existsByMemberIdAndEmail(String memberId, String email);

    Optional<Member> findByMemberId(String memberId);

    Optional<Member> findByEmail(String email);
}
