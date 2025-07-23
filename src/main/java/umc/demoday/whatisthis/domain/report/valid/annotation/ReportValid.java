package umc.demoday.whatisthis.domain.report.valid.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import umc.demoday.whatisthis.domain.report.valid.validator.ReportValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ReportValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReportValid {
    String message() default "잘못된 신고 요청입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
