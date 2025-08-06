package umc.demoday.whatisthis.domain.inquiry.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import umc.demoday.whatisthis.domain.inquiry.code.InquiryErrorCode;
import umc.demoday.whatisthis.domain.inquiry.converter.InquiryConverter;
import umc.demoday.whatisthis.domain.inquiry.dto.resDTO.InquiryAdminPageResDTO;
import umc.demoday.whatisthis.domain.inquiry.dto.resDTO.InquiryPageResDTO;
import umc.demoday.whatisthis.domain.inquiry.dto.resDTO.InquiryAdminResDTO;
import umc.demoday.whatisthis.domain.inquiry.dto.resDTO.InquiryResDTO;
import umc.demoday.whatisthis.domain.inquiry.enums.InquiryStatus;
import umc.demoday.whatisthis.domain.inquiry.repository.InquiryRepository;
import umc.demoday.whatisthis.domain.inquiry.Inquiry;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;


import static com.amazonaws.services.ec2.model.PrincipalType.Role;


@Service
@RequiredArgsConstructor
public class InquiryQueryServiceImpl implements InquiryQueryService {
    private final InquiryRepository inquiryRepository;
    private final InquiryConverter inquiryConverter;

    @Override
    public InquiryPageResDTO getInquiryList(Pageable pageable) {
        Page<Inquiry> inquiryPage;
        inquiryPage = inquiryRepository.findAll(pageable);
        return InquiryConverter.toPageDto(inquiryPage);
    }

    @Override
    public InquiryAdminPageResDTO getAdminInquiryList(Pageable pageable, String status) {
        Page<Inquiry> inquiryPage;

        if ("UNPROCESSED".equalsIgnoreCase(status)) {
            inquiryPage = inquiryRepository.findByStatus(InquiryStatus.UNPROCESSED, pageable);
        } else if ("PROCESSED".equalsIgnoreCase(status)) {
            inquiryPage = inquiryRepository.findByStatus(InquiryStatus.PROCESSED, pageable);
        } else {
            // 그 외는 전체 조회
            inquiryPage = inquiryRepository.findAll(pageable);
        }

        return InquiryConverter.toAdminPageDto(inquiryPage);
    }


    @Override
    public InquiryAdminResDTO getAdminInquiry(Integer inquiryId) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new EntityNotFoundException("해당 문의내역이 존재하지 않습니다."));

        return InquiryConverter.toAdminDto(inquiry);

    }

    @Override
    public InquiryResDTO getInquiry(Integer inquiryId, Member loginUser) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new EntityNotFoundException("해당 문의내역이 존재하지 않습니다."));

        // 비밀글이라면 접근 권한 확인
        if (inquiry.getIsSecret()) {
            if (loginUser == null) {
                throw new GeneralException(InquiryErrorCode.SECRET_INQUIRY_ACCESS_DENIED);
            }

            boolean isAuthor = inquiry.getMember().getId().equals(loginUser.getId());

            // 작성자도 아니면 접근 불가
            if (!isAuthor) {
                throw new GeneralException(InquiryErrorCode.SECRET_INQUIRY_ACCESS_DENIED);
            }
        }

        return InquiryConverter.toDto(inquiry);
    }
}