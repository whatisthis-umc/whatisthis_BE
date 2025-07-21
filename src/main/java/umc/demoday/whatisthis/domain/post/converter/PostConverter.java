package umc.demoday.whatisthis.domain.post.converter;


import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.dto.PostResponseDTO;

import java.util.List;

public class PostConverter {

    // Entity -> DTO
    public static PostResponseDTO.GgulPostResponseDTO toGgulPostResponseDTO(Post post, String category,List<String> imageUrls, List<String> hashtags, Integer postScrapCount) {
        return new PostResponseDTO.GgulPostResponseDTO(
                post.getId(),
                category,
                post.getCategory(),// 청소/이런것
                post.getTitle(),
                post.getContent(),
                hashtags,
                imageUrls,
                post.getLikeCount(),
                postScrapCount,
                post.getViewCount(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
