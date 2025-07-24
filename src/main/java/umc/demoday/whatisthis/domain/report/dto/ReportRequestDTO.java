package umc.demoday.whatisthis.domain.report.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import umc.demoday.whatisthis.domain.report.enums.ReportContent;
import umc.demoday.whatisthis.domain.report.valid.annotation.ReportValid;

public class ReportRequestDTO {

    @Getter
    @Setter
    @ReportValid
    public static class NewReportRequestDTO {

        @NotNull(message = "신고 컨텐츠는 필수입니다.")
        ReportContent content;

        String description;
    }
}
