package umc.demoday.whatisthis.domain.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.domain.report.dto.post_preview.PostPreviewDTO;
import umc.demoday.whatisthis.domain.report.enums.ReportContent;
import umc.demoday.whatisthis.domain.report.enums.ReportStatus;


import java.time.LocalDateTime;
import java.util.List;

public class AdminReportResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReportListResponseDTO {

        List<ReportPageResponseDTO> reportList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReportPageResponseDTO {

        Integer reportId;
        String type;
        String content;
        ReportContent reportContent;
        LocalDateTime reportedAt;
        ReportStatus status;

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReportDetailResponseDTO {

        Integer reportId;
        String type;

        Category category;
        String title;
        String nickname;
        String commentContent;
        LocalDateTime reportedAt;
        ReportContent content;
        String description;

        PostPreviewDTO postPreview;

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProcessResponseDTO {

        Integer reportId;
        ReportStatus status;
        Boolean isDeleted;

    }



}
