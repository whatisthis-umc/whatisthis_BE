package umc.demoday.whatisthis.domain.inquiry.service;

import org.springframework.data.domain.Pageable;
import umc.demoday.whatisthis.domain.inquiry.dto.resDTO.InquiryPageResDTO;
import umc.demoday.whatisthis.domain.inquiry.dto.resDTO.InquiryResDTO;
import umc.demoday.whatisthis.domain.inquiry.enums.InquiryStatus;


public interface InquiryQueryService {
    // InquiryPageResDTO getInquiryListAll(Pageable pageable);
    InquiryPageResDTO getInquiryList(Pageable pageable, String status);
    InquiryResDTO getInquiry(Integer id);
}
