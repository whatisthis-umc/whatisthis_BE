package umc.demoday.whatisthis.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import umc.demoday.whatisthis.domain.post.enums.Category;

import java.time.LocalDateTime;
import java.util.List;

public class AdminPostReqDTO {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class updatePostReqDTO{
        String title;
        String content;
        Category category;
        List<String> imageUrls;
    }
}
