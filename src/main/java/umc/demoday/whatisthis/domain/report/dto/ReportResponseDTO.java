package umc.demoday.whatisthis.domain.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ReportResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReportPostResponseDTO {
        Integer reportId;
        Integer postId;
        LocalDateTime reportedAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReportCommentResponseDTO {
        Integer reportId;
        Integer commentId;
        LocalDateTime reportedAt;
    }
}
