package umc.demoday.whatisthis.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import umc.demoday.whatisthis.domain.member.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    boolean existsByNickname(String nickname);

    boolean existsByMemberId(String memberId);

    boolean existsByMemberIdAndEmail(String memberId, String email);

    Optional<Member> findByMemberId(String memberId);

    Optional<Member> findByEmail(String email);

    // 오늘 가입한 유저 수
    @Query("SELECT COUNT(m) FROM Member m WHERE m.createdAt >= CURRENT_DATE")
    Integer countTodaySignups();
}
