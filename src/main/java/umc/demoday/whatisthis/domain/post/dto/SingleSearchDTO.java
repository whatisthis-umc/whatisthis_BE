package umc.demoday.whatisthis.domain.post.dto;

import lombok.*;
import org.springframework.data.domain.Page;
import umc.demoday.whatisthis.domain.admin.redis.PostDocument;
import umc.demoday.whatisthis.domain.post.enums.Category;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class SingleSearchDTO {
    List<SingleSearchDTO.SummaryDTO> results;

    public static SingleSearchDTO.SummaryDTO toSummaryDTO(PostDocument postDocument)
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

        return SingleSearchDTO.SummaryDTO.builder()
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


}
