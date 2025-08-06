package umc.demoday.whatisthis.domain.inquiry.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import umc.demoday.whatisthis.domain.inquiry.Inquiry;
import umc.demoday.whatisthis.domain.inquiry.enums.InquiryStatus;
import umc.demoday.whatisthis.domain.member.Member;

public interface InquiryRepository extends JpaRepository<Inquiry, Integer> {
    Page<Inquiry> findByStatus(InquiryStatus status, Pageable pageable);
    Page<Inquiry> findAll(Pageable pageable);

    Page<Inquiry> findAllByMember(Member member, Pageable pageable);
}
