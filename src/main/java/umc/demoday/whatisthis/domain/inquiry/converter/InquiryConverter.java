package umc.demoday.whatisthis.domain.inquiry.converter;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import umc.demoday.whatisthis.domain.inquiry.dto.reqDTO.InquiryCreateReqDTO;
import umc.demoday.whatisthis.domain.inquiry.dto.resDTO.InquiryPageResDTO;
import umc.demoday.whatisthis.domain.inquiry.dto.resDTO.InquiryResDTO;
import umc.demoday.whatisthis.domain.inquiry.Inquiry;

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
        return Inquiry.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();
    }
}
