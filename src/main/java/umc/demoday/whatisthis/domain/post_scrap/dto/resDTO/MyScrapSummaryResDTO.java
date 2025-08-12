package umc.demoday.whatisthis.domain.post_scrap.dto.resDTO;

import lombok.Getter;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post_image.PostImage;

import java.util.List;

@Getter
public class MyScrapSummaryResDTO {
    private Integer id;
    private String title;
    private String content;
    private String thumbnailUrl;
    private int viewCount;


    public MyScrapSummaryResDTO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.thumbnailUrl = post.getPostImageList().stream()
                .findFirst()
                .map(PostImage::getImageUrl)
                .orElse(null);
        this.viewCount = post.getViewCount();
    }
}