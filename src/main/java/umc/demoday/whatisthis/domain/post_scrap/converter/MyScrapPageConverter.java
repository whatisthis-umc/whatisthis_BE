package umc.demoday.whatisthis.domain.post_scrap.converter;

import org.springframework.stereotype.Component;
import org.springframework.data.domain.Page;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post_scrap.PostScrap;
import umc.demoday.whatisthis.domain.post_scrap.dto.resDTO.MyScrapPageResDTO;
import umc.demoday.whatisthis.domain.post_scrap.dto.resDTO.MyScrapSummaryResDTO;

import java.util.List;

@Component
public class MyScrapPageConverter {

    public MyScrapPageResDTO toMyScrapPageResDTO(Page<PostScrap> scrapPage) {
        List<MyScrapSummaryResDTO> dtoList = scrapPage.stream()
                .map(PostScrap::getPost)
                .map(MyScrapSummaryResDTO::new)  // Post → DTO 변환
                .toList();

        return new MyScrapPageResDTO(
                dtoList,
                scrapPage.getNumber() + 1,
                scrapPage.getSize(),
                scrapPage.getTotalPages(),
                scrapPage.getTotalElements(),
                scrapPage.isFirst(),
                scrapPage.isLast()
        );
    }
}

