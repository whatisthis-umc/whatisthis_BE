package umc.demoday.whatisthis.domain.admin.redis;


import org.springframework.stereotype.Component;
import umc.demoday.whatisthis.domain.admin.redis.dto.RedisResponseDTO;
import umc.demoday.whatisthis.domain.hashtag.Hashtag;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.domain.post_image.PostImage;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostMapper {

    public PostDocument toDocument(Post post) {
        // null 체크는 필수입니다.
        if (post == null) {
            return null;
        }

        String authorName = "익명"; // 기본값
        if (post.getMember() != null) {
            authorName = post.getMember().getNickname(); // Member의 닉네임 필드로 가정
        } else if (post.getAdmin() != null) {
            authorName = post.getAdmin().getAdminId(); // Admin의 이름 필드로 가정
        }

        List<String> images = post.getPostImageList().stream().map(PostImage::getImageUrl).toList();

        return PostDocument.builder()
                .id(post.getId().toString())
                .title(post.getTitle())
                .content(post.getContent())
                .authorName(authorName)
                .category(post.getCategory())
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .createdAt(post.getCreatedAt())
                .hashtags(post.getHashtagList().stream()
                        .map(Hashtag::getContent) // Hashtag 객체에서 키워드만 추출
                        .collect(Collectors.toSet()))
                .images(images)
                .build();
    }

    public RedisResponseDTO toRedisResponseDTO(List<PostDocument> documents) {
        // null 체크는 필수입니다.
        if (documents == null) {
            return null;
        }

        List<RedisResponseDTO.SearchResult> searchResults = documents.stream().map(document -> RedisResponseDTO.SearchResult.builder()
                .postId(document.getPostId())
                .title(document.getTitle())
                .content(document.getContent())
                .subCategory(document.getCategory())
                .category(document.getCategory().toString().endsWith("ITEM") ? "LIFE_ITEM" : "LIFE_TIP")
                .images(document.getImages())
                .build()).toList();

        return RedisResponseDTO.builder()
                .SearchResults(searchResults)
                .build();

    }
}