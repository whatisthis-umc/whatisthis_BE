package umc.demoday.whatisthis.domain.inquiry.service;


import umc.demoday.whatisthis.domain.inquiry.dto.reqDTO.InquiryCreateReqDTO;
import umc.demoday.whatisthis.domain.inquiry.dto.reqDTO.InquiryUpdateReqDTO;
import umc.demoday.whatisthis.domain.member.Member;

import java.util.List;

public interface InquiryCommandService {
    void createInquiry(InquiryCreateReqDTO dto, List<String> fileUrls, Member member);
    void updateInquiry(Integer inquiryId, InquiryUpdateReqDTO dto);
    void deleteInquiry(Integer inquiryId);
}
