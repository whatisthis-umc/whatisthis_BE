package umc.demoday.whatisthis.domain.post_scrap.dto.resDTO;

import lombok.Getter;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post_image.PostImage;

import java.util.List;

@Getter
public class MyScrapSummaryResDTO {
    private Integer id;
    private String title;
    private String content; // 필요한 필드만
    private String authorName;
    private String thumbnailUrl;


    public MyScrapSummaryResDTO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.authorName = post.getMember().getNickname();
        this.thumbnailUrl = post.getPostImageList().stream()
                .findFirst()
                .map(PostImage::getImageUrl)
                .orElse(null);
    }
}