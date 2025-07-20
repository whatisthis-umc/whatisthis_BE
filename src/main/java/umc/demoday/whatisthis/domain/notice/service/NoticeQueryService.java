package umc.demoday.whatisthis.domain.notice.service;

import org.springframework.data.domain.Pageable;
import umc.demoday.whatisthis.domain.notice.dto.resDTO.NoticePageResDTO;
import umc.demoday.whatisthis.domain.notice.dto.resDTO.NoticeResDTO;


public interface NoticeQueryService {
    NoticePageResDTO getNoticeList(Pageable pageable);
    NoticeResDTO getNotice(Integer id);
}
