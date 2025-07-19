package umc.demoday.whatisthis.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.domain.post.enums.SortBy;

import java.time.LocalDateTime;
import java.util.List;

public class PostResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommunityPostPreviewDTO {
        Integer id;
        String title;
        String content;
        Category category;
        String nickname;
        LocalDateTime createdAt;
        Boolean isBestUser;
        Integer viewCount;
        Integer likeCount;
        Integer commentCount;
        List <String> imageUrl;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommunityPostPreviewListDTO {
        List<CommunityPostPreviewDTO> postList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommunityPostImageURLDTO {
        String url;
    }


}
