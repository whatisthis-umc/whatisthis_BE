package umc.demoday.whatisthis.domain.inquiry.dto.resDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import umc.demoday.whatisthis.domain.inquiry.Inquiry;
import umc.demoday.whatisthis.domain.inquiry.enums.InquiryStatus;


import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class InquiryAdminResDTO {
    private Integer id;
    private String title;
    private String content;
    private InquiryStatus status;
    private LocalDateTime createdAt;
    private String answerContent;

    public static InquiryAdminResDTO from(Inquiry inquiry) {
        return InquiryAdminResDTO.builder()
                .id(inquiry.getId())
                .title(inquiry.getTitle())
                .content(inquiry.getContent())
                .status(inquiry.getStatus())
                .createdAt(inquiry.getCreatedAt())
                .answerContent(inquiry.getAnswer() != null ? inquiry.getAnswer().getContent() : null)
                .build();
    }
}