package umc.demoday.whatisthis.domain.post_scrap.dto.resDTO;

import lombok.Getter;
import umc.demoday.whatisthis.domain.post.Post;

import java.util.List;

@Getter
public class MyScrapPageResDTO {

    private final List<MyScrapSummaryResDTO> scraps;  // Post DTO 리스트로 변경
    private final int pageNumber;
    private final int pageSize;
    private final int totalPages;
    private final long totalElements;
    private final boolean isFirst;
    private final boolean isLast;

    public MyScrapPageResDTO(List<MyScrapSummaryResDTO> scraps,
                             int pageNumber,
                             int pageSize,
                             int totalPages,
                             long totalElements,
                             boolean isFirst,
                             boolean isLast) {
        this.scraps = scraps;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.isFirst = isFirst;
        this.isLast = isLast;
    }
}