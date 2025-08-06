package umc.demoday.whatisthis.domain.inquiry.converter;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import umc.demoday.whatisthis.domain.inquiry.dto.reqDTO.InquiryCreateReqDTO;
import umc.demoday.whatisthis.domain.inquiry.dto.resDTO.InquiryAdminPageResDTO;
import umc.demoday.whatisthis.domain.inquiry.dto.resDTO.InquiryAdminResDTO;
import umc.demoday.whatisthis.domain.inquiry.Inquiry;
import umc.demoday.whatisthis.domain.inquiry.dto.resDTO.InquiryPageResDTO;
import umc.demoday.whatisthis.domain.inquiry.dto.resDTO.InquiryResDTO;
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
                inquiry.getCreatedAt(),
                inquiry.getMember().getNickname(),
                inquiry.getAnswer() != null ? inquiry.getAnswer().getContent() : null,
                inquiry.getIsSecret()
        );
    }

    public static InquiryAdminResDTO toAdminDto(Inquiry inquiry) {
        return new InquiryAdminResDTO(
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

    public static InquiryAdminPageResDTO toAdminPageDto(Page<Inquiry> page) {
        List<InquiryAdminResDTO> dtoList = page.stream()
                .map(InquiryConverter::toAdminDto)
                .toList();

        return new InquiryAdminPageResDTO(
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

        return inquiry;
    }
}
