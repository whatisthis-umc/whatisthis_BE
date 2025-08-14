package umc.demoday.whatisthis.domain.notice.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.demoday.whatisthis.domain.admin.Admin;
import umc.demoday.whatisthis.domain.admin.repository.AdminRepository;
import umc.demoday.whatisthis.domain.notice.Notice;
import umc.demoday.whatisthis.domain.notice.converter.NoticeConverter;
import umc.demoday.whatisthis.domain.notice.dto.reqDTO.NoticeCreateReqDTO;
import umc.demoday.whatisthis.domain.notice.dto.reqDTO.NoticeUpdateReqDTO;
import umc.demoday.whatisthis.domain.notice.repository.NoticeRepository;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;

import static umc.demoday.whatisthis.domain.notice.code.NoticeErrorCode.NOTICE_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeCommandServiceImpl implements NoticeCommandService {

    private final NoticeRepository noticeRepository;
    private final AdminRepository adminRepository;

    @Override
    public void createNotice(NoticeCreateReqDTO dto, Integer adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("Admin not found: " + adminId));
        Notice notice = NoticeConverter.toEntity(dto);
        notice.setAdmin(admin);
        noticeRepository.save(notice);
    }

    @Override
    public void updateNotice(Integer noticeId, NoticeUpdateReqDTO dto) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new GeneralException(NOTICE_NOT_FOUND));

        // 필요한 필드만 수정
        notice.update(dto.getTitle(), dto.getContent());
    }

    @Override
    public void deleteNotice(Integer noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new GeneralException(NOTICE_NOT_FOUND));
        noticeRepository.deleteById(notice.getId());
    }
}