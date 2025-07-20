package umc.demoday.whatisthis.domain.notice.service;


import umc.demoday.whatisthis.domain.notice.dto.reqDTO.NoticeCreateReqDTO;

public interface NoticeCommandService {
    void createNotice(NoticeCreateReqDTO dto);
}
