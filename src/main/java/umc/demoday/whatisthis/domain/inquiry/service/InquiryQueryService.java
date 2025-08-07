package umc.demoday.whatisthis.domain.inquiry.service;

import org.springframework.data.domain.Pageable;
import umc.demoday.whatisthis.domain.inquiry.dto.resDTO.InquiryAdminPageResDTO;
import umc.demoday.whatisthis.domain.inquiry.dto.resDTO.InquiryAdminResDTO;
import umc.demoday.whatisthis.domain.inquiry.dto.resDTO.InquiryPageResDTO;
import umc.demoday.whatisthis.domain.inquiry.dto.resDTO.InquiryResDTO;
import umc.demoday.whatisthis.domain.member.Member;


public interface InquiryQueryService {
    InquiryPageResDTO getInquiryList(Pageable pageable);
    InquiryAdminPageResDTO getAdminInquiryList(Pageable pageable, String status);
    InquiryAdminResDTO getAdminInquiry(Integer id);
    InquiryResDTO getInquiry(Integer id, Member loginUser);
}
