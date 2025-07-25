package umc.demoday.whatisthis.domain.report.dto.post_preview;

import umc.demoday.whatisthis.domain.post.enums.Category;

import java.util.List;

public class GgulPostPreviewDTO implements PostPreviewDTO {

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


}
