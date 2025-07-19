package umc.demoday.whatisthis.domain.post.converter;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.dto.PostRequestDTO;
import umc.demoday.whatisthis.domain.post.dto.PostResponseDTO;
import umc.demoday.whatisthis.domain.post.service.PostService;
import umc.demoday.whatisthis.domain.post_image.PostImage;

import java.util.List;
import java.util.stream.Collectors;

public class PostConverter {

    PostService postService;

    public static PostResponseDTO.CommunityPostPreviewDTO toCommunityPostPreviewDTO(Post post) {

        List<String> imageUrls = post.getPostImageList().stream()
                .map(PostImage::getImageUrl)
                .collect(Collectors.toList());

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
                .isBestUser(post.getMember().getIsBest())
                .commentCount(post.getCommentList().size())
                .imageUrl(imageUrls)
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

    public static Post toNewPost (PostRequestDTO.NewPostRequestDTO request) {

        /*
        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .category(request.getCategory())
                .build();

        List<PostImage> postImages = request.getImageUrls().stream()
                .map(url -> PostImage.builder()
                        .imageUrl(url)
                        .post(post) // 연관관계 주입
                        .build())
                .collect(Collectors.toList());
        */

        return null;
    }

    public static PostResponseDTO.NewPostResponseDTO toNewPostDTO(Post post) {
        return null;
    }
}
