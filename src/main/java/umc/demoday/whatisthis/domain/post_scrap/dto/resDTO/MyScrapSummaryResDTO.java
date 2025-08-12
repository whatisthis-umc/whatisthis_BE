package umc.demoday.whatisthis.domain.post_scrap.dto.resDTO;

import lombok.Getter;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.domain.post_image.PostImage;

import java.util.List;

@Getter
public class MyScrapSummaryResDTO {
    private Integer postId;
    private String title;
    private String content;
    private String thumbnailUrl;
    private int viewCount;
    private Category category;
    private Category subCategory;


    public MyScrapSummaryResDTO(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.thumbnailUrl = post.getPostImageList().stream()
                .findFirst()
                .map(PostImage::getImageUrl)
                .orElse(null);
        this.viewCount = post.getViewCount();
        // subCategory는 그대로
        this.subCategory = post.getCategory();

        // category는 category 이름 끝나는 글자에 따라 다르게 설정
        String categoryName = post.getCategory().name();

        if (categoryName.endsWith("_TIP")) {
            this.category = Category.LIFE_TIP;  // 원하는 enum 값으로 변경
        } else if (categoryName.endsWith("_ITEM")) {
            this.category = Category.LIFE_ITEM;  // 원하는 enum 값으로 변경
        } else {
            this.category = post.getCategory();  // 기본값 유지
        }
    }
}