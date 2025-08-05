package umc.demoday.whatisthis.domain.inquiry.converter;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import umc.demoday.whatisthis.domain.file.File;
import umc.demoday.whatisthis.domain.inquiry.dto.reqDTO.InquiryCreateReqDTO;
import umc.demoday.whatisthis.domain.inquiry.dto.resDTO.InquiryPageResDTO;
import umc.demoday.whatisthis.domain.inquiry.dto.resDTO.InquiryResDTO;
import umc.demoday.whatisthis.domain.inquiry.Inquiry;
import umc.demoday.whatisthis.domain.inquiry.enums.InquiryStatus;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class InquiryConverter {
    public static InquiryResDTO toDto(Inquiry inquiry) {
        return new InquiryResDTO(
                inquiry.getId(),
                inquiry.getTitle(),
                inquiry.getContent(),
                inquiry.getStatus(),
                inquiry.getCreatedAt()
        );
    }

    public static InquiryPageResDTO toPageDto(Page<Inquiry> page) {
        List<InquiryResDTO> dtoList = page.stream()
                .map(InquiryConverter::toDto)
                .toList();

        return new InquiryPageResDTO(
                dtoList,
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.isFirst(),
                page.isLast()
        );
    }

    public static Inquiry toEntity(InquiryCreateReqDTO dto) {
        Inquiry inquiry = Inquiry.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .isSecret(dto.getIsSecret())
                .status(InquiryStatus.UNPROCESSED) // 기본 상태 설정
                .createdAt(LocalDateTime.now())    // 날짜 설정
                .build();

        // 파일 URL 리스트가 있는 경우 처리
//        if (dto.getFileUrls() != null && !dto.getFileUrls().isEmpty()) {
//            for (String url : dto.getFileUrls()) {
//                File file = new File();
//                file.setUrl(url);
//                file.setInquiry(inquiry); // 양방향 연관관계 설정
//                inquiry.getFiles().add(file); // Inquiry 쪽에도 추가
//            }
//        }

        return inquiry;
    }
}
