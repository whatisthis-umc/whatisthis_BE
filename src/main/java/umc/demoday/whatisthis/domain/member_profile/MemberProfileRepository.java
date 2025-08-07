package umc.demoday.whatisthis.domain.member_profile;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberProfileRepository extends JpaRepository<MemberProfile, Integer> {
    // Member의 ID를 통해 MemberProfile을 찾는 쿼리 메소드
    Optional<MemberProfile> findByMember_Id(Integer memberId);
}
