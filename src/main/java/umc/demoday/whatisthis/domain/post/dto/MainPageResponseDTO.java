package umc.demoday.whatisthis.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public class MainPageResponseDTO {

    private List<CategoryDTO> categories;
    private List<SectionDTO> sections;

    // 카테고리 정보를 담는 DTO
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryDTO {
        private String category; // "ALL", "COOK_TIP" 등 문자열을 받기 위함
    }

    // 각 섹션 정보를 담는 DTO
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SectionDTO {
        private String sectionName;
        private List<PostResponseDTO.GgulPostSummaryDTO> posts;
        private MoreDTO more;
    }

    // 각 섹션에 포함된 게시물 요약 정보를 담는 DTO => PostResponseDTO.GgulPostSummaryDTO 재활용하기


    // '더보기' 링크 정보를 담는 DTO
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MoreDTO {
        private String url;
    }
}
