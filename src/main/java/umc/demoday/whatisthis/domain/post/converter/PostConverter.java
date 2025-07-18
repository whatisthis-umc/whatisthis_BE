package umc.demoday.whatisthis.domain.post.converter;

import org.springframework.data.domain.Page;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.dto.PostResponseDTO;

import java.util.List;

public class PostConverter {

    public static PostResponseDTO.CommunityPostPreviewDTO toCommunityPostPreviewDTO(Post post) {

        return PostResponseDTO.CommunityPostPreviewDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory())
                .nickname(post.getMember().getNickname())
                .createdAt(post.getCreatedAt())
                .build();
    }

    public static PostResponseDTO.CommunityPostPreviewListDTO toCommunityPostPreviewListDTO(Page<Post> postList) {
        return null;
    }

}
