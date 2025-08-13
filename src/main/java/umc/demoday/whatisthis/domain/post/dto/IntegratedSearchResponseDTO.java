package umc.demoday.whatisthis.domain.post.dto;

import lombok.*;
import org.springframework.data.domain.Page;
import umc.demoday.whatisthis.domain.admin.redis.PostDocument;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.enums.Category;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class IntegratedSearchResponseDTO {

    List<SectionDTO> sections;

    public static IntegratedSearchResponseDTO toIntegratedSearchResponseDTO(Page<PostDocument> tipSearch, Page<PostDocument> itemSection, Page<PostDocument> communitySection) {
       List<SummaryDTO> tipSummary = tipSearch.getContent().stream()
               .map(IntegratedSearchResponseDTO::toSummaryDTO)
               .toList();
       List<SummaryDTO> itemSummary = itemSection.getContent().stream()
               .map(IntegratedSearchResponseDTO::toSummaryDTO)
               .toList();
       List<SummaryDTO> communitySummary = communitySection.getContent().stream()
               .map(IntegratedSearchResponseDTO::toSummaryDTO)
               .toList();

        List<SectionDTO> sections = new ArrayList<>();
        sections.add(toSectionDTO(tipSummary,"생활꿀팁 검색 결과", "posts/search/single?category=LIFE_TIP&page=1&size=6"));
        sections.add(toSectionDTO(itemSummary, "생활꿀템 검색 결과", "posts/search/single?category=LIFE_ITEM&page=1&size=6"));
        sections.add(toSectionDTO(communitySummary,"커뮤니티 검색 결과", "posts/search/single?page=1&size=6"));
        return new IntegratedSearchResponseDTO(sections);
    }

    public static SummaryDTO toSummaryDTO(PostDocument postDocument)
    {

        String summary = postDocument.getContent();
        if(summary.length() > 30)
            summary = summary.substring(0, 30);

        String thumbnailUrl = null; // 기본값은 null
        if (postDocument.getImages() != null && !postDocument.getImages().isEmpty()) {
            //안전할 때만 첫 번째 이미지를 가져옴
            thumbnailUrl = postDocument.getImages().get(0);
        }

        Category mainCategory = postDocument.getCategory();
        if(mainCategory.toString().endsWith("_TIP"))
            mainCategory = Category.LIFE_TIP;
        else if(mainCategory.toString().endsWith("_ITEM"))
            mainCategory = Category.LIFE_ITEM;

        return SummaryDTO.builder()
                .postId(Integer.parseInt(postDocument.getId()))
                .thumnailUrl(thumbnailUrl)
                .summary(summary)
                .category(mainCategory)
                .subCategory(postDocument.getCategory())
                .title(postDocument.getTitle())
                .hashtags(postDocument.getHashtags().stream().toList())
                .viewCount(postDocument.getViewCount())
                .likeCount(postDocument.getLikeCount())
                .createdAt(postDocument.getCreatedAt())
                .build();
    }

    public static SectionDTO toSectionDTO(List<SummaryDTO> postSummaries, String sectionName, String more) {
        return new SectionDTO(
                sectionName,
                postSummaries,
                more
        );
    }

    @Getter
    @Builder
    public static class SummaryDTO {
        Integer postId;
        String thumnailUrl;
        Category category;
        Category subCategory;
        String title;
        String summary;
        List<String> hashtags;
        Integer viewCount;
        Integer likeCount;
        LocalDateTime createdAt;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SectionDTO {
        private String sectionName;
        private List<SummaryDTO> posts;
        private String more;
    }
}
