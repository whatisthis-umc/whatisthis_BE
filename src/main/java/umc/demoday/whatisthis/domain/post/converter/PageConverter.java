package umc.demoday.whatisthis.domain.post.converter;

import org.springframework.data.domain.Page;
import umc.demoday.whatisthis.domain.hashtag.Hashtag;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.dto.PostResponseDTO;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.domain.post.enums.SortBy;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PageConverter {
    // Page -> GgulPostsByCategoryResponseDTO

    public static PostResponseDTO.GgulPostsByCategoryResponseDTO ggulPostsByCategoryResponseDTO(Page<Post> post, String imageUrl, List<Hashtag> hashtags, String summary,Integer scrapCount) {
            List<PostResponseDTO.GgulPostSummaryDTO> summaryDTOList = post.getContent().stream()
                    .map(posts -> new PostResponseDTO.GgulPostSummaryDTO(
                        posts.getId(), //  Integer postId;
                        imageUrl,//  String thumnailUrl;
                        posts.getTitle(),//  String title
                        summary, //  String summary;
                        hashtags,//  List<Hashtag> hashtags;
                        posts.getViewCount(),//  Integer viewCount;
                        posts.getLikeCount(),//  Integer likeCount;
                        scrapCount,//  Integer scrapCount;
                        posts.getCreatedAt()//  LocalDateTime createdAt;
                    )
                    );
        return new PostResponseDTO.GgulPostsByCategoryResponseDTO(
        );

        public static class GgulPostsByCategoryResponseDTO {
            Category category;
            SortBy sortBy;
            Integer page;
            Integer size;
            Integer totalElements;
            Integer totalPages;
            List<PostResponseDTO.GgulPostSummaryDTO> posts;
        }
    }
}
