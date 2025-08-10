package umc.demoday.whatisthis.domain.member.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import umc.demoday.whatisthis.domain.member.dto.member.MemberReqDTO;
import umc.demoday.whatisthis.domain.member.validation.annotation.PasswordMatches;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    private String first;
    private String second;

    @Override
    public void initialize(PasswordMatches ann) {
        this.first = ann.first();
        this.second = ann.second();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext ctx) {
        if (value == null) return true; // @NotNull이 따로 잡음
        var w = new org.springframework.beans.BeanWrapperImpl(value);
        Object a = w.getPropertyValue(first);
        Object b = w.getPropertyValue(second);

        boolean ok = a != null && a.equals(b);
        if (!ok) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(ctx.getDefaultConstraintMessageTemplate())
                    .addPropertyNode(second) // 두 번째 필드에 에러 표시
                    .addConstraintViolation();
        }
        return ok;
    }
}
