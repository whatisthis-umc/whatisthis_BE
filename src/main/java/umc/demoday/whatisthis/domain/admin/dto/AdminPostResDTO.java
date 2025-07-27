package umc.demoday.whatisthis.domain.admin.dto;

import lombok.*;
import umc.demoday.whatisthis.domain.admin.Admin;
import umc.demoday.whatisthis.domain.post.enums.Category;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class AdminPostResDTO {
    Integer postId;
    String title;
    String content;
    Category category;
    String nickname;
    LocalDateTime createdAt;
    Integer viewCount;
    Integer scrapCount;
    List<String> imageUrls;

    @Setter
    @Getter
    @Builder
    @AllArgsConstructor
    public static class updatePostResDTO{
        Integer postId;
        String title;
        String content;
        Category category;
        List<String> imageUrls;
        LocalDateTime updatedAt;
    }

    @Setter
    @Getter
    @Builder
    @AllArgsConstructor
    public static class createPostResDTO{
        Integer postId;
        String title;
        String content;
        Category category;
        List<String> imageUrls;
        LocalDateTime createdAt;
    }

    @Setter
    @Getter
    @Builder
    @AllArgsConstructor
    public static class getAllPostResDTO{
        Integer postId;
        String title;
        String content;
        Category category;
        LocalDateTime createdAt;
    }

    @Setter
    @Getter
    @Builder
    @AllArgsConstructor
    public static class allPostResDTO{
        List<getAllPostResDTO> posts;
        Integer page;
        Integer size;
        Integer totalPages;
        Long totalElements;
    }

}
