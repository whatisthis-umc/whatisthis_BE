package umc.demoday.whatisthis.domain.inquiry.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.demoday.whatisthis.domain.file.File;
import umc.demoday.whatisthis.domain.inquiry.converter.InquiryConverter;
import umc.demoday.whatisthis.domain.inquiry.dto.reqDTO.InquiryCreateReqDTO;
import umc.demoday.whatisthis.domain.inquiry.dto.reqDTO.InquiryUpdateReqDTO;
import umc.demoday.whatisthis.domain.inquiry.enums.InquiryStatus;
import umc.demoday.whatisthis.domain.inquiry.repository.InquiryRepository;
import umc.demoday.whatisthis.domain.inquiry.Inquiry;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static umc.demoday.whatisthis.domain.inquiry.code.InquiryErrorCode.INQUIRY_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class InquiryCommandServiceImpl implements InquiryCommandService {

    private final InquiryRepository inquiryRepository;

    @Override
    public void createInquiry(InquiryCreateReqDTO dto, List<String> fileUrls, Member member) {
        Inquiry inquiry = Inquiry.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .isSecret(dto.getIsSecret())
                .status(InquiryStatus.UNPROCESSED) // 기본 상태 설정
                .createdAt(LocalDateTime.now())    // 날짜 설정
                .member(member)
                .build();

        // 파일 URL 리스트를 InquiryFile 엔티티로 변환
        List<File> files = fileUrls.stream()
                .map(url -> File.builder()
                        .url(url)
                        .inquiry(inquiry)  // 연관관계 설정
                        .build())
                .collect(Collectors.toList());

        // Inquiry에 파일들 추가
        inquiry.addFiles(files);  // 내부에서 this.files.addAll(files) 같은 로직

        // 저장 (Cascade.ALL 덕분에 InquiryFile도 함께 저장됨)
        inquiryRepository.save(inquiry);
    }

    @Override
    public void updateInquiry(Integer inquiryId, InquiryUpdateReqDTO dto) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new GeneralException(INQUIRY_NOT_FOUND));

        // 필요한 필드만 수정
        inquiry.update(dto.getTitle(), dto.getContent());
    }

    @Override
    public void deleteInquiry(Integer inquiryId) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new GeneralException(INQUIRY_NOT_FOUND));
        inquiryRepository.deleteById(inquiry.getId());
    }
}