package umc.demoday.whatisthis.domain.notice.dto.resDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import umc.demoday.whatisthis.domain.notice.Notice;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class NoticeResDTO {
    private Integer id;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    public static NoticeResDTO from(Notice notice) {
        return NoticeResDTO.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt())
                .build();
    }
}