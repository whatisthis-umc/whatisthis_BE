package umc.demoday.whatisthis.domain.qna.converter;



import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import umc.demoday.whatisthis.domain.qna.Qna;
import umc.demoday.whatisthis.domain.qna.dto.reqDTO.QnaCreateReqDTO;
import umc.demoday.whatisthis.domain.qna.dto.resDTO.QnaPageResDTO;
import umc.demoday.whatisthis.domain.qna.dto.resDTO.QnaResDTO;

import java.util.List;

@Component
public class QnaConverter {
    public static QnaResDTO toDto(Qna qna) {
        return new QnaResDTO(
                qna.getId(),
                qna.getTitle(),
                qna.getContent(),
                qna.getCreatedAt()
        );
    }

    public static QnaPageResDTO toPageDto(Page<Qna> page) {
        List<QnaResDTO> dtoList = page.stream()
                .map(QnaConverter::toDto)
                .toList();

        return new QnaPageResDTO(
                dtoList,
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.isFirst(),
                page.isLast()
        );
    }

    public static Qna toEntity(QnaCreateReqDTO dto) {
        return Qna.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();
    }
}
