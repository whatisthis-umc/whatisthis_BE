package umc.demoday.whatisthis.domain.admin.dto;

import lombok.*;
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
}
