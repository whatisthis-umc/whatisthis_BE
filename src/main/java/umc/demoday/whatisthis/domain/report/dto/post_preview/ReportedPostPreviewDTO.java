package umc.demoday.whatisthis.domain.report.dto.post_preview;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.demoday.whatisthis.domain.post.enums.Category;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportedPostPreviewDTO implements PostPreviewDTO {

    Integer postId;
    String title;
    Category category;
    String content;
    List<String> hashtags;
    List<String> images;

}
