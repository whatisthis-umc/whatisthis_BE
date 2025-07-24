package umc.demoday.whatisthis.domain.post.valid.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.domain.post.valid.validator.EnumValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValidator.class)
public @interface EnumValid {
    Category[] anyOf();
    String message() default "올바르지 않은 카테고리입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
