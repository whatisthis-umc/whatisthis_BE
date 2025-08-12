package umc.demoday.whatisthis.domain.profile_image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.profile_image.ProfileImage;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Integer> {
    boolean existsByMember(Member member);

    void deleteByMember(Member member);

    @Modifying
    @Query("DELETE FROM ProfileImage p WHERE p.member.id = :memberId")
    void deleteByMemberId(@Param("memberId") Integer memberId);
}
