package umc.demoday.whatisthis.domain.report.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import umc.demoday.whatisthis.domain.report.Report;
import umc.demoday.whatisthis.domain.report.enums.ReportStatus;

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

    @Query("SELECT r FROM Report r " +
            "LEFT JOIN r.post p " +
            "LEFT JOIN r.comment c " +
            "WHERE r.status = :status " +
            "AND (LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "     OR LOWER(c.content) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Report> findByStatusAndKeyword(@Param("status") ReportStatus status,
                                        @Param("keyword") String keyword,
                                        Pageable pageable);


    @Query("SELECT r FROM Report r " +
            "LEFT JOIN r.post p " +
            "LEFT JOIN r.comment c " +
            "WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(c.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Report> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    Page<Report> findByStatus(ReportStatus status, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Report r SET r.post = null WHERE r.post.id = :postId")
    void detachAllByPostId(@Param("postId") Integer postId);
}
