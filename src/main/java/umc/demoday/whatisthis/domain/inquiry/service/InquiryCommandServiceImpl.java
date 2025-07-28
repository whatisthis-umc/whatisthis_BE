package umc.demoday.whatisthis.domain.inquiry.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.demoday.whatisthis.domain.inquiry.converter.InquiryConverter;
import umc.demoday.whatisthis.domain.inquiry.dto.reqDTO.InquiryCreateReqDTO;
import umc.demoday.whatisthis.domain.inquiry.dto.reqDTO.InquiryUpdateReqDTO;
import umc.demoday.whatisthis.domain.inquiry.repository.InquiryRepository;
import umc.demoday.whatisthis.domain.inquiry.Inquiry;

@Service
@RequiredArgsConstructor
@Transactional
public class InquiryCommandServiceImpl implements InquiryCommandService {

    private final InquiryRepository inquiryRepository;

    @Override
    public void createInquiry(InquiryCreateReqDTO dto) {
        Inquiry inquiry = InquiryConverter.toEntity(dto);
        inquiryRepository.save(inquiry);
    }

    @Override
    public void updateInquiry(Integer inquiryId, InquiryUpdateReqDTO dto) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new EntityNotFoundException("문의내역을 찾을 수 없습니다."));

        // 필요한 필드만 수정
        inquiry.update(dto.getTitle(), dto.getContent());
    }

    @Override
    public void deleteInquiry(Integer inquiryId) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new EntityNotFoundException("문의내역을 찾을 수 없습니다."));
        inquiryRepository.deleteById(inquiry.getId());
    }
}