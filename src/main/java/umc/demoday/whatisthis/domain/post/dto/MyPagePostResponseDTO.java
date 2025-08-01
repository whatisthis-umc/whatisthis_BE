package umc.demoday.whatisthis.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.demoday.whatisthis.domain.post.enums.Category;

import java.time.LocalDateTime;
import java.util.List;

public class MyPagePostResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyPostDTO {
        Integer id;
        Category category;
        String title;
        String content;
        String nickname;
        LocalDateTime createdAt;
        Integer viewCount;
        Integer likeCount;
        Integer commentCount;
        List<String> postImageUrls;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyPostPageDTO {

        String nickname;
        String profileImageUrl;
        String email;
        List<MyPostDTO> posts;
    }


}
