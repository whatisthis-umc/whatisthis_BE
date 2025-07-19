package umc.demoday.whatisthis.domain.notice.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class NoticePageResDTO {

    private final List<NoticeResDTO> notices;
    private final int pageNumber;       // 클라이언트 기준 1부터 시작
    private final int pageSize;
    private final int totalPages;
    private final long totalElements;
    private final boolean isFirst;
    private final boolean isLast;

    public NoticePageResDTO(List<NoticeResDTO> notices,
                            int pageNumber,
                            int pageSize,
                            int totalPages,
                            long totalElements,
                            boolean isFirst,
                            boolean isLast) {
        this.notices = notices;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.isFirst = isFirst;
        this.isLast = isLast;
    }
}
