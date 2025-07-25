package umc.demoday.whatisthis.domain.report.dto.post_preview;

import umc.demoday.whatisthis.domain.post.dto.PostResponseDTO;

import java.time.LocalDateTime;

public class CommPostPreviewDTO implements PostPreviewDTO {

    Integer postId;
    String title;
    String category;
    String content;
    String commentContent;

}
