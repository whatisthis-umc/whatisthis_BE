package umc.demoday.whatisthis.domain.report.dto.post_preview;

import java.util.List;

public class ReportedPostPreviewDTO implements PostPreviewDTO {

    Integer postId;
    String title;
    String category;
    String content;
    List<String> hashtags;
    List<String> images;
    String commentContent;

}
