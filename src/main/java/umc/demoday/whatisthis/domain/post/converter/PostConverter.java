package umc.demoday.whatisthis.domain.post.converter;


import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.dto.PostResponseDTO;

import java.util.List;

public class PostConverter {

}
    // Entity -> DTO
    public static PostResponseDTO.GgulPostResponseDTO toGgulPostResponseDTO(Post post) {
        return new PostResponseDTO.GgulPostResponseDTO(
                post.getId(),
                post.getCategory(),
                post.getSubCategory(),
                post.getTitle(),
                post.getContent(),
                post.getHashtags(),
                post.getImages(),
                post.getLikeCount(),
                post.getScrapCount(),
                post.getViewCount(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getTimpstamp()
        );
}
