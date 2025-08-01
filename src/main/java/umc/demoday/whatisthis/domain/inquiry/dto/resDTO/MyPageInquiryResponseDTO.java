package umc.demoday.whatisthis.domain.inquiry.dto.resDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.demoday.whatisthis.domain.inquiry.enums.InquiryStatus;

import java.time.LocalDateTime;
import java.util.List;

public class MyPageInquiryResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class myInquiryDTO {
        Integer id;
        String title;
        InquiryStatus status;
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyInquiryPageDTO {

        String nickname;
        String profileImageUrl;
        String email;
        List<myInquiryDTO> inquiries;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class myInquiryDetailDTO {
        Integer id;
        String title;
        String inquiryContent;
        String answerContent;
        LocalDateTime createdAt;
    }

}
