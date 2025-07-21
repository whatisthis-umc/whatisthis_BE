package umc.demoday.whatisthis.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
        String subCategories; //Post에 서브 카테고리 List<String> 타입 추가하면 List<String>으로 타입 변
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

        //timestamp는 공통 응답 구조에 추가
    }
}
