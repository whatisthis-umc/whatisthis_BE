package umc.demoday.whatisthis.domain.answer.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import umc.demoday.whatisthis.domain.admin.Admin;
import umc.demoday.whatisthis.domain.admin.repository.AdminRepository;
import umc.demoday.whatisthis.domain.answer.Answer;
import umc.demoday.whatisthis.domain.answer.converter.AnswerConverter;
import umc.demoday.whatisthis.domain.answer.dto.reqDTO.AnswerRegisterReqDTO;
import umc.demoday.whatisthis.domain.answer.repository.AnswerRepository;
import umc.demoday.whatisthis.domain.inquiry.Inquiry;
import umc.demoday.whatisthis.domain.inquiry.enums.InquiryStatus;
import umc.demoday.whatisthis.domain.inquiry.repository.InquiryRepository;

import static umc.demoday.whatisthis.domain.inquiry.code.InquiryErrorCode.INQUIRY_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AnswerCommandServiceImpl implements AnswerCommandService {

    private final AnswerRepository answerRepository;
    private final InquiryRepository inquiryRepository;
    private final AdminRepository adminRepository;

    @Override
    @Transactional
    public void registerAnswer(Integer inquiryId, AnswerRegisterReqDTO dto) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "문의내역을 찾을 수 없습니다."));

        // 관리자 고정 ID 예시 (예: 1)
        Admin admin = adminRepository.findById(1)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "관리자를 찾을 수 없습니다."));

        Answer answer = Answer.builder()
                .content(dto.getContent())
                .admin(admin)
                .inquiry(inquiry)
                .build();

        answerRepository.save(answer);

        inquiry.setStatus(InquiryStatus.PROCESSED);
    }

}