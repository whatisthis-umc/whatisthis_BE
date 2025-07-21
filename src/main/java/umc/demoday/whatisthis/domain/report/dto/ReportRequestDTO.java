package umc.demoday.whatisthis.domain.report.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import umc.demoday.whatisthis.domain.report.enums.Content;

public class ReportRequestDTO {

    @Getter
    @Setter
    public static class NewReportRequestDTO {

        @NotBlank
        Content content;

        String description;
    }
}
