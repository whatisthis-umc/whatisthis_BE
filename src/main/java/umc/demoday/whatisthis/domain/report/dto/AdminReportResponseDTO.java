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
    public static class reportListResponseDTO {

        List<reportPageResponseDTO> reportList;
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
    public static class reportPageResponseDTO {

        String type;
        ReportContent content;
        LocalDateTime reportedAt;
        ReportStatus status;

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class reportDetailResponseDTO {

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
