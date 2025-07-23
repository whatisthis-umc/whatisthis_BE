package umc.demoday.whatisthis.domain.notice.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.demoday.whatisthis.domain.notice.Notice;
import umc.demoday.whatisthis.domain.notice.converter.NoticeConverter;
import umc.demoday.whatisthis.domain.notice.dto.reqDTO.NoticeCreateReqDTO;
import umc.demoday.whatisthis.domain.notice.dto.reqDTO.NoticeUpdateReqDTO;
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

    @Override
    public void updateNotice(Integer noticeId, NoticeUpdateReqDTO dto) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new EntityNotFoundException("공지사항을 찾을 수 없습니다."));

        // 필요한 필드만 수정
        notice.update(dto.getTitle(), dto.getContent());
    }

    @Override
    public void deleteNotice(Integer noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new EntityNotFoundException("공지사항을 찾을 수 없습니다."));
        noticeRepository.deleteById(notice.getId());
    }
}