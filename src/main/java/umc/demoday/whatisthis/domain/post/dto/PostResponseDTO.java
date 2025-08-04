package umc.demoday.whatisthis.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import umc.demoday.whatisthis.domain.hashtag.Hashtag;
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
    public static class CommunityHashtagDTO {
        String content;
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
        Category category;
        CommunityPostHashtagListDTO hashtagListDto;
        CommunityPostImageListDTO imageListDto;
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
    public static class CommunityPostImageListDTO {
        List<CommunityPostImageURLDTO> imageList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommunityPostHashtagListDTO {
        List<CommunityHashtagDTO> hashtagList;
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
        Long totalElements;
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
        Category category;
        Category subCategory;
        String title;
        String summary;
        List<String> hashtags;
        Integer viewCount;
        Integer likeCount;
        Integer scrapCount;
        LocalDateTime createdAt;
        // timestamp는 공통 응답 구조에 추가
    }

}
