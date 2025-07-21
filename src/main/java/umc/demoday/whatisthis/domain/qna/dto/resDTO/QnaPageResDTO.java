package umc.demoday.whatisthis.domain.qna.dto.resDTO;

import lombok.Getter;
import umc.demoday.whatisthis.domain.notice.dto.resDTO.NoticeResDTO;

import java.util.List;

@Getter
public class QnaPageResDTO {

    private final List<QnaResDTO> qnas;
    private final int pageNumber;       // 클라이언트 기준 1부터 시작
    private final int pageSize;
    private final int totalPages;
    private final long totalElements;
    private final boolean isFirst;
    private final boolean isLast;

    public QnaPageResDTO(List<QnaResDTO> qnas,
                            int pageNumber,
                            int pageSize,
                            int totalPages,
                            long totalElements,
                            boolean isFirst,
                            boolean isLast) {
        this.qnas = qnas;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.isFirst = isFirst;
        this.isLast = isLast;
    }
}
