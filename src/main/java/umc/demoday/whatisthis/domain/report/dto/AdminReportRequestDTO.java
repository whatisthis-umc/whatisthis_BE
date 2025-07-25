package umc.demoday.whatisthis.domain.report.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class AdminReportRequestDTO {

    @Getter @Setter
    public static class ProcessRequestDTO {

        @NotNull
        Boolean delete;

    }
}
