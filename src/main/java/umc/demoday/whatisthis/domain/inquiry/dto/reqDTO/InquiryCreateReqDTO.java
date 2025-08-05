package umc.demoday.whatisthis.domain.inquiry.dto.reqDTO;

import lombok.Getter;

import java.util.List;

@Getter
public class InquiryCreateReqDTO {
    private String title;
    private String content;
    private Boolean isSecret;
    // private List<String> fileUrls; // ← 첨부파일 URL 리스트
}