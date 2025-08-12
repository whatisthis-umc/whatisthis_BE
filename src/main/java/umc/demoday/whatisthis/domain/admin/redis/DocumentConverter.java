package umc.demoday.whatisthis.domain.admin.redis;

import org.springframework.data.domain.Page;
import umc.demoday.whatisthis.domain.post.Post;

import java.util.List;

public class DocumentConverter {

    public static Page<Post> toPagePosts(Page<PostDocument> posts) {
        List<Post> postList = posts.getContent().stream()
                .map(postDocument -> Post.builder()
                        .
                        .build())
    }
}
