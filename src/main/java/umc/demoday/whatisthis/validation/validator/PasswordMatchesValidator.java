package umc.demoday.whatisthis.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import umc.demoday.whatisthis.dto.member.MemberReqDTO;
import umc.demoday.whatisthis.validation.annotation.PasswordMatches;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, MemberReqDTO.JoinRequestDTO> {

    @Override
    public boolean isValid(MemberReqDTO.JoinRequestDTO dto, ConstraintValidatorContext context) {
        if (dto.getPassword() == null || dto.getPasswordCheck() == null) {
            return false;
        }
        return dto.getPassword().equals(dto.getPasswordCheck());
    }
}
