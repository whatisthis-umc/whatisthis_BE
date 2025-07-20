package umc.demoday.whatisthis.domain.qna.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import umc.demoday.whatisthis.domain.notice.Notice;
import umc.demoday.whatisthis.domain.notice.converter.NoticeConverter;
import umc.demoday.whatisthis.domain.notice.dto.resDTO.NoticePageResDTO;
import umc.demoday.whatisthis.domain.notice.dto.resDTO.NoticeResDTO;
import umc.demoday.whatisthis.domain.notice.repository.NoticeRepository;
import umc.demoday.whatisthis.domain.qna.Qna;
import umc.demoday.whatisthis.domain.qna.converter.QnaConverter;
import umc.demoday.whatisthis.domain.qna.dto.resDTO.QnaPageResDTO;
import umc.demoday.whatisthis.domain.qna.dto.resDTO.QnaResDTO;
import umc.demoday.whatisthis.domain.qna.repository.QnaRepository;

@Service
@RequiredArgsConstructor
public class QnaQueryServiceImpl implements QnaQueryService {
    private final QnaRepository qnaRepository;
    private final QnaConverter qnaConverter;

    @Override
    public QnaPageResDTO getQnaList(Pageable pageable) {
        Page<Qna> qnaPage = qnaRepository.findAll(pageable);
        return QnaConverter.toPageDto(qnaPage);
    }

    @Override
    public QnaResDTO getQna(Integer qnaId) {
        Qna qna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new EntityNotFoundException("해당 qna가 존재하지 않습니다."));

        return QnaConverter.toDto(qna);

    }
}
