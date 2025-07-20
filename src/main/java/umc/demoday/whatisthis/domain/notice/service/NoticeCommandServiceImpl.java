package umc.demoday.whatisthis.domain.notice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.demoday.whatisthis.domain.notice.Notice;
import umc.demoday.whatisthis.domain.notice.converter.NoticeConverter;
import umc.demoday.whatisthis.domain.notice.dto.reqDTO.NoticeCreateReqDTO;
import umc.demoday.whatisthis.domain.notice.repository.NoticeRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeCommandServiceImpl implements NoticeCommandService {

    private final NoticeRepository noticeRepository;

    @Override
    public void createNotice(NoticeCreateReqDTO dto) {
        Notice notice = NoticeConverter.toEntity(dto);
        noticeRepository.save(notice);
    }

}