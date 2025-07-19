package umc.demoday.whatisthis.domain.notice.converter;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import umc.demoday.whatisthis.domain.notice.Notice;
import umc.demoday.whatisthis.domain.notice.dto.NoticePageResDTO;
import umc.demoday.whatisthis.domain.notice.dto.NoticeResDTO;

import java.util.List;

@Component
public class NoticeConverter {
    public static NoticeResDTO toDto(Notice notice) {
        return new NoticeResDTO(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getCreatedAt()
        );
    }

    public static NoticePageResDTO toPageDto(Page<Notice> page) {
        List<NoticeResDTO> dtoList = page.stream()
                .map(NoticeConverter::toDto)
                .toList();

        return new NoticePageResDTO(
                dtoList,
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.isFirst(),
                page.isLast()
        );
    }
}
