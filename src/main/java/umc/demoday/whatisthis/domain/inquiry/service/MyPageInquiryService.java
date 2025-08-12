package umc.demoday.whatisthis.domain.inquiry.service;

import org.springframework.data.domain.Page;
import umc.demoday.whatisthis.domain.inquiry.Inquiry;
import umc.demoday.whatisthis.domain.member.Member;

public interface MyPageInquiryService {

    Page<Inquiry> getMyInquiries(Integer page, Integer size, Member member);
    Inquiry getInquiryByIdAndMember(Integer id, Member member);
    void deleteInquiryById(Integer id, Member member);
}
