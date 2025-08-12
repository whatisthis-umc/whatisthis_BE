package umc.demoday.whatisthis.domain.notice.service;

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
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;

import static umc.demoday.whatisthis.domain.notice.code.NoticeErrorCode.NOTICE_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class NoticeQueryServiceImpl implements NoticeQueryService {
    private final NoticeRepository noticeRepository;
    private final NoticeConverter noticeConverter;

    @Override
    public NoticePageResDTO getNoticeList(Pageable pageable) {
        Page<Notice> noticePage = noticeRepository.findAll(pageable);
        return NoticeConverter.toPageDto(noticePage);
    }

    @Override
    public NoticeResDTO getNotice(Integer noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new GeneralException(NOTICE_NOT_FOUND));

        return NoticeConverter.toDto(notice);

    }
}