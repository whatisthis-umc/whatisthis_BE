package umc.demoday.whatisthis.domain.inquiry.dto.resDTO;

import lombok.Getter;

import java.util.List;

@Getter
public class InquiryPageResDTO {

    private final List<InquiryResDTO> inquiries;
    private final int pageNumber;       // 클라이언트 기준 1부터 시작
    private final int pageSize;
    private final int totalPages;
    private final long totalElements;
    private final boolean isFirst;
    private final boolean isLast;

    public InquiryPageResDTO(List<InquiryResDTO> inquiries,
                             int pageNumber,
                             int pageSize,
                             int totalPages,
                             long totalElements,
                             boolean isFirst,
                             boolean isLast) {
        this.inquiries = inquiries;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.isFirst = isFirst;
        this.isLast = isLast;
    }
}
