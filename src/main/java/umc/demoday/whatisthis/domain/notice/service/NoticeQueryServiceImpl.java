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
                .orElseThrow(() -> new EntityNotFoundException("해당 공지사항이 존재하지 않습니다."));

        return NoticeConverter.toDto(notice);

    }
}