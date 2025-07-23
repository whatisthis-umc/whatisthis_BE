package umc.demoday.whatisthis.domain.qna.dto.resDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import umc.demoday.whatisthis.domain.qna.Qna;


import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class QnaResDTO {
    private Integer id;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    public static QnaResDTO from(Qna qna) {
        return QnaResDTO.builder()
                .id(qna.getId())
                .title(qna.getTitle())
                .content(qna.getContent())
                .createdAt(qna.getCreatedAt())
                .build();
    }
}

