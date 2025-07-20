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

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NewPostResponseDTO {
        Integer id;
        LocalDateTime createdAt;
        Integer authorId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommunityPostViewDTO {
        Integer id;
        String title;
        String content;
        String nickname;
        Boolean isBestUser;
        String profileimageUrl;
        Integer viewCount;
        Integer likeCount;
        Integer commentCount;
        LocalDateTime createdAt;
        CommentViewListDTO commentListDto;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentViewListDTO {
        List<CommentViewDTO> commentList;
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
    public static class CommentViewDTO {
        Integer id;
        String content;
        Integer likeCount;
        String nickname;
        String profileimageUrl;
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostLikeCountDTO {
        Integer likeCount;
    }

}
