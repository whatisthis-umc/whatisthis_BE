package umc.demoday.whatisthis.domain.qna.service;

import org.springframework.data.domain.Pageable;
import umc.demoday.whatisthis.domain.qna.dto.resDTO.QnaPageResDTO;
import umc.demoday.whatisthis.domain.qna.dto.resDTO.QnaResDTO;

public interface QnaQueryService {
    QnaPageResDTO getQnaList(Pageable pageable);
    QnaResDTO getQna(Integer id);
}
