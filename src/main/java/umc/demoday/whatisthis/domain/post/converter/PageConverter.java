package umc.demoday.whatisthis.domain.post.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import umc.demoday.whatisthis.domain.hashtag.Hashtag;
import umc.demoday.whatisthis.domain.hashtag.repository.HashtagRepository;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.dto.PostResponseDTO;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.domain.post.enums.SortBy;
import umc.demoday.whatisthis.domain.post_image.PostImage;
import umc.demoday.whatisthis.domain.post_image.repository.PostImageRepository;
import umc.demoday.whatisthis.domain.post_scrap.repository.PostScrapRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PageConverter {

    private final PostImageRepository postImageRepository;
    private final PostScrapRepository postScrapRepository;
    private final HashtagRepository hashtagRepository;

    public PostResponseDTO.GgulPostsByCategoryResponseDTO toGgulPostsByCategoryResponseDTO(Page<Post> postPage, Category category, SortBy sortBy) {
        List<Post> posts = postPage.getContent();

        // 게시글이 없는 경우 빈 응답을 즉시 반환
        if (posts.isEmpty()) {
            return new PostResponseDTO.GgulPostsByCategoryResponseDTO(
                    category,
                    sortBy,
                    postPage.getNumber(),
                    postPage.getSize(),
                    0L,
                    0,
                    Collections.emptyList()
            );
        }
        // 1. 페이지 내 모든 게시글 ID 추출
        List<Integer> postIds = posts.stream()
                .map(Post::getId)
                .collect(Collectors.toList());

        // 2. 연관 데이터 일괄 조회 (Batch Fetching)
        Map<Integer, String> thumbnailsMap = findThumbnails(postIds);
        Map<Integer, List<Hashtag>> hashtagsMap = findHashtags(postIds);
        Map<Integer, Integer> scrapCountsMap = getScrapCountMap(postIds);

        // 3. DTO 리스트 생성 (DB 추가 조회 없음)
        List<PostResponseDTO.GgulPostSummaryDTO> summaryDTOList = posts.stream()
                .map(post -> toGgulPostSummaryDTO(
                        post,
                        thumbnailsMap.get(post.getId()),
                        hashtagsMap.getOrDefault(post.getId(), Collections.emptyList()),
                        scrapCountsMap.getOrDefault(post.getId(), 0)
                ))
                .collect(Collectors.toList());

        return new PostResponseDTO.GgulPostsByCategoryResponseDTO(
                category,
                sortBy,
                postPage.getNumber(), // page index
                postPage.getSize(),
                postPage.getTotalElements(),
                postPage.getTotalPages(),
                summaryDTOList
        );
    }

    // Post 엔티티를 GgulPostSummaryDTO로 변환
    private PostResponseDTO.GgulPostSummaryDTO toGgulPostSummaryDTO(Post post, String thumbnailUrl, List<Hashtag> hashtags, Integer scrapCount) {
        String summary = post.getContent();
        // 내용이 30자 이상일 경우 자르기 (Null-safe)
        if (summary != null && summary.length() > 30) {
            summary = summary.substring(0, 30) + "...";
        }

        return new PostResponseDTO.GgulPostSummaryDTO(
                post.getId(),
                thumbnailUrl, // 조회된 썸네일 URL
                post.getTitle(),
                summary,
                hashtags, // 조회된 해시태그 리스트
                post.getViewCount(),
                post.getLikeCount(),
                scrapCount, // 조회된 스크랩 수
                post.getCreatedAt()
        );
    }

    /**
     * 주어진 Post ID 리스트에 해당하는 썸네일들을 한 번의 쿼리로 조회합니다.
     */
    private Map<Integer, String> findThumbnails(List<Integer> postIds) {
        // 각 post의 첫 번째 이미지를 썸네일로 간주
        return postImageRepository.findAllByPost_IdIn(postIds).stream()
                .collect(Collectors.toMap(
                        postImage-> postImage.getPost().getId(), // post 객체에서 id 가져옴
                        PostImage::getImageUrl,
                        (existing, replacement) -> existing // 중복 시 첫 번째 값 유지
                ));
    }

    // 주어진 Post ID 리스트에 해당하는 해시태그들을 한 번의 쿼리로 조회
    private Map<Integer, List<Hashtag>> findHashtags(List<Integer> postIds) {
        return hashtagRepository.findAllByPost_IdIn(postIds).stream()
                .collect(Collectors.groupingBy(hashtag -> hashtag.getPost().getId())); // Hashtag 객체에서 id 가져옴
    }

    // 주어진 Post ID 리스트에 해당하는 스크랩 수 카운팅
    private Map<Integer, Integer> getScrapCountMap(List<Integer> postIds) {
        return postScrapRepository.findScrapCountsByPostIds(postIds)
                .stream()
                .collect(Collectors.toMap(
                        result -> (Integer) result[0],    // postId
                        result -> (Integer) result[1]) // count
                );
    }


}