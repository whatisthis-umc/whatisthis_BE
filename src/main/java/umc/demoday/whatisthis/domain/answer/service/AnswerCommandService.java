package umc.demoday.whatisthis.domain.answer.service;

import umc.demoday.whatisthis.domain.admin.Admin;
import umc.demoday.whatisthis.domain.answer.dto.reqDTO.AnswerRegisterReqDTO;

public interface AnswerCommandService {

    void registerAnswer(Integer inquiryId, AnswerRegisterReqDTO dto);
}