package umc.demoday.whatisthis.domain.qna.service;

import umc.demoday.whatisthis.domain.notice.dto.reqDTO.NoticeCreateReqDTO;
import umc.demoday.whatisthis.domain.notice.dto.reqDTO.NoticeUpdateReqDTO;
import umc.demoday.whatisthis.domain.qna.Qna;
import umc.demoday.whatisthis.domain.qna.dto.reqDTO.QnaCreateReqDTO;
import umc.demoday.whatisthis.domain.qna.dto.reqDTO.QnaUpdateReqDTO;

public interface QnaCommandService {
    void createQna(QnaCreateReqDTO dto);
    void updateQna(Integer qnaId, QnaUpdateReqDTO dto);
    void deleteQna(Integer qnaId);
}
