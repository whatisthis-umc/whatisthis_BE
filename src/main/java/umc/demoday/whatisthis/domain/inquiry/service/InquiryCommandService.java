package umc.demoday.whatisthis.domain.inquiry.service;


import umc.demoday.whatisthis.domain.inquiry.dto.reqDTO.InquiryCreateReqDTO;
import umc.demoday.whatisthis.domain.inquiry.dto.reqDTO.InquiryUpdateReqDTO;

public interface InquiryCommandService {
    void createInquiry(InquiryCreateReqDTO dto);
    void updateInquiry(Integer inquiryId, InquiryUpdateReqDTO dto);
    void deleteInquiry(Integer inquiryId);
}
