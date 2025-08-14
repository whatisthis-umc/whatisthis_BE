package umc.demoday.whatisthis.domain.post.converter;

import org.springframework.data.domain.Page;
import umc.demoday.whatisthis.domain.comment.Comment;
import umc.demoday.whatisthis.domain.hashtag.Hashtag;
import umc.demoday.whatisthis.domain.member.Member;

import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.dto.PostRequestDTO;
import umc.demoday.whatisthis.domain.post.dto.PostResponseDTO;
import umc.demoday.whatisthis.domain.post_image.PostImage;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PostConverter {

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

    public static Post toNewPost (PostRequestDTO.NewPostRequestDTO request, Member loginMember) {

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .viewCount(0)
                .likeCount(0)
                .category(request.getCategory())
                .member(loginMember)
                .build();

//        List<PostImage> postImages = request.getImageUrls().stream()
//                .map(url -> PostImage.builder()
//                        .imageUrl(url)
//                        .post(post)
//                        .build())
//                .collect(Collectors.toList());

        List<Hashtag> hashtags = request.getHashtags().stream()
                        .map(hashtag -> Hashtag.builder()
                                .content(hashtag)
                                .post(post)
                                .build())
                .collect(Collectors.toList());

        // post.setPostImageList(postImages);
        post.setHashtagList(hashtags);

        return post;
    }

    public static PostResponseDTO.NewPostResponseDTO toNewPostDTO(Post post) {
        return PostResponseDTO.NewPostResponseDTO.builder()
                .id(post.getId())
                .createdAt(post.getCreatedAt())
                .authorId(post.getMember().getId())
                .build();
    }

    public static PostResponseDTO.CommunityPostViewDTO toCommunityPostViewDTO(Post post, Page<Comment> commentList, List<PostImage> imageList, List<Hashtag> hashtagList) {


        List<PostResponseDTO.CommentViewDTO> commentDtoList = commentList.stream()
                .map(comment -> PostResponseDTO.CommentViewDTO.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .likeCount(comment.getLikeCount())
                        .nickname(comment.getMember().getNickname())
                        .profileimageUrl(
                                comment.getMember().getProfileImage() != null
                                        ? comment.getMember().getProfileImage().getImageUrl()
                                        : null
                        )
                        .parentId(
                                comment.getParent() != null ? comment.getParent().getId() : null
                        )
                        .createdAt(comment.getCreatedAt())
                        .build())
                .toList();

        PostResponseDTO.CommentViewListDTO commentViewListDTO =
                PostResponseDTO.CommentViewListDTO.builder()
                        .commentList(commentDtoList)
                        .listSize(commentDtoList.size())
                        .totalPage(commentList.getTotalPages())
                        .totalElements(commentList.getTotalElements())
                        .isFirst(commentList.isFirst())
                        .isLast(commentList.isLast())
                        .build();

        List<PostResponseDTO.CommunityPostImageURLDTO> imageDtoList =
                (imageList == null ? Collections.<PostImage>emptyList() : imageList)
                        .stream()
                        .map(img -> PostResponseDTO.CommunityPostImageURLDTO.builder()
                                .url(img.getImageUrl())
                                .build())
                        .toList();

        PostResponseDTO.CommunityPostImageListDTO imageListDto =
                PostResponseDTO.CommunityPostImageListDTO.builder()
                        .imageList(imageDtoList)
                        .build();

        List<PostResponseDTO.CommunityHashtagDTO> hashtagDtoList =
                (hashtagList == null ? Collections.<Hashtag>emptyList() : hashtagList)
                        .stream()
                        .map(ht -> PostResponseDTO.CommunityHashtagDTO.builder()
                                .content(ht.getContent())
                                .build())
                        .toList();

        PostResponseDTO.CommunityPostHashtagListDTO hashtagListDto =
                PostResponseDTO.CommunityPostHashtagListDTO.builder()
                        .hashtagList(hashtagDtoList)
                        .build();


        String profileImageUrl = post.getMember().getProfileImage() != null
                ? post.getMember().getProfileImage().getImageUrl()
                : null;

        return PostResponseDTO.CommunityPostViewDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory())
                .hashtagListDto(hashtagListDto)
                .imageListDto(imageListDto)
                .nickname(post.getMember().getNickname())
                .isBestUser(post.getMember().getIsBest())
                .profileimageUrl(profileImageUrl)
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentList().size())
                .createdAt(post.getCreatedAt())
                .commentListDto(commentViewListDTO)
                .build();
    }



    public static PostResponseDTO.PostLikeCountDTO toPostLikeCountDTO(Post post) {
        return PostResponseDTO.PostLikeCountDTO.builder()
                .likeCount(post.getLikeCount())
                .build();
    }

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
