package umc.demoday.whatisthis.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import umc.demoday.whatisthis.dto.member.MemberReqDTO;
import umc.demoday.whatisthis.validation.annotation.PasswordMatches;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, MemberReqDTO.JoinRequestDTO> {

    @Override
    public boolean isValid(MemberReqDTO.JoinRequestDTO dto, ConstraintValidatorContext context) {
        if (dto == null) {
            return true;
        }

        String password = dto.getPassword();
        String passwordCheck = dto.getPasswordCheck();

        boolean valid = password != null && password.equals(passwordCheck);

        if (!valid) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate("비밀번호가 일치하지 않습니다.")
                    .addPropertyNode("passwordCheck")
                    .addConstraintViolation();
        }
        return valid;
    }
}
