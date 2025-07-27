package umc.demoday.whatisthis.domain.answer.converter;

import umc.demoday.whatisthis.domain.admin.Admin;
import umc.demoday.whatisthis.domain.answer.Answer;
import umc.demoday.whatisthis.domain.answer.dto.reqDTO.AnswerRegisterReqDTO;
import umc.demoday.whatisthis.domain.inquiry.Inquiry;

public class AnswerConverter {

    public static Answer toEntity(AnswerRegisterReqDTO dto, Admin admin, Inquiry inquiry) {
        return Answer.builder()
                .content(dto.getContent())
                .admin(admin)
                .inquiry(inquiry)
                .build();
    }

//    public static AnswerResDTO toDTO(Answer answer) {
//        return AnswerResDTO.builder()
//                .answerId(answer.getId())
//                .content(answer.getContent())
//                .adminName(answer.getAdmin().getName())
//                .createdAt(answer.getCreatedAt())
//                .build();
//    }
}