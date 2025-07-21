package umc.demoday.whatisthis.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import umc.demoday.whatisthis.domain.hashtag.Hashtag;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.domain.post.enums.SortBy;

import java.time.LocalDateTime;
import java.util.List;

public class PostResponseDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GgulPostResponseDTO {
        Integer postId;
        String category;
        Category subCategories; //post 엔티티의 category
        String title;
        String content;
        List<String> hashtags;
        List<String> images;
        int likeCount;
        int scrapCount;
        int viewCount;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
        LocalDateTime createdAt;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
        LocalDateTime updatedAt;

        //timestamp는 공통 응답 구조에 추가 예정
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GgulPostsByCategoryResponseDTO {
        Category category;
        SortBy  sortBy;
        Integer page;
        Integer size;
        Integer totalElements;
        Integer totalPages;
        List<PostResponseDTO.GgulPostSummaryDTO> posts;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GgulPostSummaryDTO {
        Integer postId;
        String thumnailUrl;
        String title;
        String summary;
        List<Hashtag> hashtags;
        Integer viewCount;
        Integer likeCount;
        Integer scrapCount;
        LocalDateTime createdAt;
        // timestamp는 공통 응답 구조에 추가
    }
}
