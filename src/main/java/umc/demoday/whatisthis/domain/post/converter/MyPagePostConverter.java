package umc.demoday.whatisthis.domain.post.converter;

import org.springframework.data.domain.Page;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.dto.MyPagePostResponseDTO;
import umc.demoday.whatisthis.domain.post_image.PostImage;
import umc.demoday.whatisthis.domain.profile_image.ProfileImage;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode.MEMBER_NOT_FOUND;

public class MyPagePostConverter {

    public static MyPagePostResponseDTO.MyPostPageDTO toMyPostPageDTO (Page<Post> postList) {

        List<MyPagePostResponseDTO.MyPostDTO> myPostDTOList = postList.stream()
                .map(MyPagePostConverter::toMyPostDTO).toList();

        Member author = postList.getContent().stream()
                .map(Post::getMember)
                .findFirst()
                .orElseThrow(() -> new GeneralException(MEMBER_NOT_FOUND));

        // 3) ProfileImage가 null일 수 있으므로 안전하게 꺼내기
        String profileImageUrl = Optional.ofNullable(author.getProfileImage())
                .map(ProfileImage::getImageUrl)
                .orElse(null);

        return MyPagePostResponseDTO.MyPostPageDTO.builder()
                .nickname(author.getNickname())
                .profileImageUrl(profileImageUrl)
                .email(author.getEmail())
                .posts(myPostDTOList)
                .build();
    }

    public static MyPagePostResponseDTO.MyPostDTO toMyPostDTO (Post post) {

        List<String> postImageUrls = post.getPostImageList()
                .stream()
                .map(PostImage::getImageUrl)
                .collect(Collectors.toList());

        return MyPagePostResponseDTO.MyPostDTO.builder()
                .postId(post.getId())
                .category(post.getCategory())
                .title(post.getTitle())
                .content(post.getContent())
                .nickname(post.getMember().getNickname())
                .createdAt(post.getCreatedAt())
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentList().size())
                .postImageUrls(postImageUrls)
                .build();
    }

}
