package umc.demoday.whatisthis.domain.report.dto.post_preview;

import umc.demoday.whatisthis.domain.post.enums.Category;

import java.util.List;

public class ReportedCommentPreviewDTO implements PostPreviewDTO {

    Integer postId;
    Category category;
    String title;
    String content;
    List<String> hashtags;
    List<String> images;


}
