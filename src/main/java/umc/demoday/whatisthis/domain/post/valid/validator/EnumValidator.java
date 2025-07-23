package umc.demoday.whatisthis.domain.post.valid.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.domain.post.valid.annotation.EnumValid;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class EnumValidator implements ConstraintValidator<EnumValid, Category> {

    private Set<Category> subset;

    @Override
    public void initialize(EnumValid constraintAnnotation) {
        subset = Arrays.stream(constraintAnnotation.anyOf()).collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(Category value, ConstraintValidatorContext context) {
        return value != null && subset.contains(value);
    }
}
