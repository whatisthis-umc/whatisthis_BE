package umc.demoday.whatisthis.domain.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.demoday.whatisthis.domain.report.Report;

public interface ReportRepository extends JpaRepository<Report, Integer> {

    @Query("""
    SELECT COUNT(r) 
    FROM Report r 
    WHERE 
        (r.post IS NOT NULL AND r.post.member.id = :memberId)
        OR
        (r.comment IS NOT NULL AND r.comment.member.id = :memberId)
""")
    Integer countReportsByReportedMemberId(@Param("memberId") Integer memberId);

}
