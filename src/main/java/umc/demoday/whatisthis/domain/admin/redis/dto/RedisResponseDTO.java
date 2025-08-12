package umc.demoday.whatisthis.domain.admin.redis.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.domain.post_image.PostImage;

import java.util.List;

@Getter
@Builder
public class RedisResponseDTO {


    @Getter
    @Builder
    public static class SearchResult {
        Integer postId;
        String category;
        Category subCategory;  //post 엔티티의 category
        String title;
        String content;
        List<String> images;
    }

   List<SearchResult> SearchResults;
}
