package umc.demoday.whatisthis.domain.notice.service;


import umc.demoday.whatisthis.domain.notice.dto.reqDTO.NoticeCreateReqDTO;
import umc.demoday.whatisthis.domain.notice.dto.reqDTO.NoticeUpdateReqDTO;

public interface NoticeCommandService {
    void createNotice(NoticeCreateReqDTO dto, Integer adminId);
    void updateNotice(Integer noticeId, NoticeUpdateReqDTO dto);
    void deleteNotice(Integer noticeId);
}
