package umc.demoday.whatisthis.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.demoday.whatisthis.domain.member.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    boolean existsByNickname(String nickname);

    boolean existsByMemberId(String memberId);

    boolean existsByMemberIdAndEmail(String memberId, String email);

    Optional<Member> findByMemberId(String memberId);

    Optional<Member> findByEmail(String email);

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.profileImage WHERE m.id = :id")
    Optional<Member> findByIdWithProfileImage(@Param("id") Integer id);
}
