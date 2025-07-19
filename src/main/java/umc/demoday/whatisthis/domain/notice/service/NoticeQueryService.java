package umc.demoday.whatisthis.domain.notice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import umc.demoday.whatisthis.domain.notice.Notice;
import umc.demoday.whatisthis.domain.notice.dto.NoticePageResDTO;
import umc.demoday.whatisthis.domain.notice.dto.NoticeResDTO;


public interface NoticeQueryService {
    NoticePageResDTO getNoticeList(Pageable pageable);
    NoticeResDTO getNotice(Integer id);
}
