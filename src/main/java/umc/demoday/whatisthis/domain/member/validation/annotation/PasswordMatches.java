package umc.demoday.whatisthis.domain.member.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import umc.demoday.whatisthis.domain.member.validation.validator.PasswordMatchesValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatches {
    String message() default "비밀번호가 일치하지 않습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
