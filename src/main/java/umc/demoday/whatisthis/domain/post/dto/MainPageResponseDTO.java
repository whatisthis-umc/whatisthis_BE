package umc.demoday.whatisthis.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import umc.demoday.whatisthis.domain.post.enums.Category;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MainPageResponseDTO {

    private List<Category> categories;
    private List<SectionDTO> sections;


    // 각 섹션 정보를 담는 DTO
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SectionDTO {
        private String sectionName;
        private List<PostResponseDTO.GgulPostSummaryDTO> posts;
        private String more;
    }

    // 각 섹션에 포함된 게시물 요약 정보를 담는 DTO => PostResponseDTO.GgulPostSummaryDTO 재활용하기

}
