package umc.demoday.whatisthis.domain.post.converter;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.dto.PostResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class PostConverter {

    public static PostResponseDTO.CommunityPostPreviewDTO toCommunityPostPreviewDTO(Post post) {

        return PostResponseDTO.CommunityPostPreviewDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory())
                .nickname(post.getMember().getNickname())
                .createdAt(post.getCreatedAt())
                .isBestUser(post.getMember().getIsBest())
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .isBestUser(null)
                .commentCount(null)
                .imageUrl(null)
                .build();
    }

    public static PostResponseDTO.CommunityPostPreviewListDTO toCommunityPostPreviewListDTO(Page<Post> postList) {

        List<PostResponseDTO.CommunityPostPreviewDTO> postPreViewDTOList = postList.stream()
                .map(PostConverter::toCommunityPostPreviewDTO).collect(Collectors.toList());

        return PostResponseDTO.CommunityPostPreviewListDTO.builder()
                .isLast(postList.isLast())
                .isFirst(postList.isFirst())
                .totalPage(postList.getTotalPages())
                .totalElements(postList.getTotalElements())
                .listSize(postPreViewDTOList.size())
                .postList(postPreViewDTOList)
                .build();
    }

}
