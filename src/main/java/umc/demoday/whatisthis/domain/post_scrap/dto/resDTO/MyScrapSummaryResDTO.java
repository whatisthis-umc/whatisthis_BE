package umc.demoday.whatisthis.domain.post_scrap.dto.resDTO;

import lombok.Getter;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.domain.post_image.PostImage;
import umc.demoday.whatisthis.domain.post_scrap.PostScrap;

import java.util.List;

@Getter
public class MyScrapSummaryResDTO {
    private Integer id;
    private Integer postId;
    private String title;
    private String content;
    private String thumbnailUrl;
    private int viewCount;
    private Category category;
    private Category subCategory;


    public MyScrapSummaryResDTO(PostScrap scrap) {
        this.id = scrap.getId();                  // 스크랩 아이디
        Post post = scrap.getPost();              // 스크랩이 가진 포스트 정보

        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.thumbnailUrl = post.getPostImageList().stream()
                .findFirst()
                .map(PostImage::getImageUrl)
                .orElse(null);
        this.viewCount = post.getViewCount();
        this.subCategory = post.getCategory();

        String categoryName = post.getCategory().name();
        if (categoryName.endsWith("_TIP")) {
            this.category = Category.LIFE_TIP;
        } else if (categoryName.endsWith("_ITEM")) {
            this.category = Category.LIFE_ITEM;
        } else {
            this.category = post.getCategory();
        }
    }
}
