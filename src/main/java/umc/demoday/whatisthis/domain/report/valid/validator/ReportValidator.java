package umc.demoday.whatisthis.domain.report.valid.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import umc.demoday.whatisthis.domain.report.dto.ReportRequestDTO;
import umc.demoday.whatisthis.domain.report.enums.ReportContent;
import umc.demoday.whatisthis.domain.report.valid.annotation.ReportValid;

public class ReportValidator implements ConstraintValidator<ReportValid, ReportRequestDTO.NewReportRequestDTO> {

    @Override
    public void initialize(ReportValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(ReportRequestDTO.NewReportRequestDTO dto, ConstraintValidatorContext context) {
        if (dto.getContent() == null) return false;

        if (dto.getContent() == ReportContent.ETC_CONTENT) {
            if (dto.getDescription() == null || dto.getDescription().isBlank()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("기타 신고 사유를 작성해야 합니다.")
                        .addPropertyNode("description").addConstraintViolation();
                return false;
            }
        }
        else {
            if (dto.getDescription() != null && !dto.getDescription().isBlank()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("기타 신고 외에는 description을 작성할 수 없습니다.")
                        .addPropertyNode("description").addConstraintViolation();
                return false;
            }
        }

        return true;
    }

}
