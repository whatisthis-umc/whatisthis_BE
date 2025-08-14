package umc.demoday.whatisthis.domain.qna.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.demoday.whatisthis.domain.admin.Admin;
import umc.demoday.whatisthis.domain.admin.repository.AdminRepository;
import umc.demoday.whatisthis.domain.qna.Qna;
import umc.demoday.whatisthis.domain.qna.converter.QnaConverter;
import umc.demoday.whatisthis.domain.qna.dto.reqDTO.QnaCreateReqDTO;
import umc.demoday.whatisthis.domain.qna.dto.reqDTO.QnaUpdateReqDTO;
import umc.demoday.whatisthis.domain.qna.repository.QnaRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class QnaCommandServiceImpl implements QnaCommandService {

    private final QnaRepository qnaRepository;
    private final AdminRepository adminRepository;

    @Override
    public void createQna(QnaCreateReqDTO dto, Integer adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("Admin not found: " + adminId));
        Qna qna = QnaConverter.toEntity(dto);
        qna.setAdmin(admin);
        qnaRepository.save(qna);
    }

    @Override
    public void updateQna(Integer qnaId, QnaUpdateReqDTO dto) {
        Qna qna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new EntityNotFoundException("qna를 찾을 수 없습니다."));

        // 필요한 필드만 수정
        qna.update(dto.getTitle(), dto.getContent());
    }

    @Override
    public void deleteQna(Integer qnaId) {
        Qna qna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new EntityNotFoundException("qna를 찾을 수 없습니다."));
        qnaRepository.deleteById(qna.getId());
    }
}
