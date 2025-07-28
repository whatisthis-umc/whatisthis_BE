package umc.demoday.whatisthis.domain.inquiry.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import umc.demoday.whatisthis.domain.inquiry.converter.InquiryConverter;
import umc.demoday.whatisthis.domain.inquiry.dto.resDTO.InquiryPageResDTO;
import umc.demoday.whatisthis.domain.inquiry.dto.resDTO.InquiryResDTO;
import umc.demoday.whatisthis.domain.inquiry.enums.InquiryStatus;
import umc.demoday.whatisthis.domain.inquiry.repository.InquiryRepository;
import umc.demoday.whatisthis.domain.inquiry.Inquiry;


@Service
@RequiredArgsConstructor
public class InquiryQueryServiceImpl implements InquiryQueryService {
    private final InquiryRepository inquiryRepository;
    private final InquiryConverter inquiryConverter;

//    @Override
//    public InquiryPageResDTO getInquiryListAll(Pageable pageable) {
//
//    }

    @Override
    public InquiryPageResDTO getInquiryList(Pageable pageable, String status) {
        Page<Inquiry> inquiryPage;

        if ("UNPROCESSED".equalsIgnoreCase(status)) {
            inquiryPage = inquiryRepository.findByStatus(InquiryStatus.UNPROCESSED, pageable);
        } else if ("PROCESSED".equalsIgnoreCase(status)) {
            inquiryPage = inquiryRepository.findByStatus(InquiryStatus.PROCESSED, pageable);
        } else {
            // 그 외는 전체 조회
            inquiryPage = inquiryRepository.findAll(pageable);
        }

        return InquiryConverter.toPageDto(inquiryPage);
    }


    @Override
    public InquiryResDTO getInquiry(Integer inquiryId) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new EntityNotFoundException("해당 문의내역이 존재하지 않습니다."));

        return InquiryConverter.toDto(inquiry);

    }
}