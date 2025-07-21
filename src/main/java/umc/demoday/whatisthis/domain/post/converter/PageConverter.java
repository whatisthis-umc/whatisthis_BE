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
import umc.demoday.whatisthis.domain.post_image.repository.PostImageRepository;
import umc.demoday.whatisthis.domain.post_scrap.repository.PostScrapRepository;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PageConverter {

    private final PostImageRepository postImageRepository;
    private final PostScrapRepository postScrapRepository;
    private final HashtagRepository hashtagRepository;

    // Page -> GgulPostsByCategoryResponseDTO

    public PostResponseDTO.GgulPostsByCategoryResponseDTO toGgulPostsByCategoryResponseDTO(Page<Post> post,Category category, SortBy sortBy, Integer page) {
            List<PostResponseDTO.GgulPostSummaryDTO> summaryDTOList = post.getContent().stream()
                    .map(posts -> new PostResponseDTO.GgulPostSummaryDTO(
                        posts.getId(), //  Integer postId;
                        postImageRepository.findOneByPostId(posts.getId()).getImageUrl(),//  String thumnailUrl;
                        posts.getTitle(),//  String title
                        posts.getContent().substring(0,30), //  String summary;
                        hashtagRepository.findAllByPostId(posts.getId()).stream().toList(),//  List<Hashtag> hashtags;
                        posts.getViewCount(),//  Integer viewCount;
                        posts.getLikeCount(),//  Integer likeCount;
                        postScrapRepository.countByPostId(posts.getId()),//  Integer scrapCount;
                        posts.getCreatedAt()//  LocalDateTime createdAt;
                    )
                    ).collect(Collectors.toList());
        return new PostResponseDTO.GgulPostsByCategoryResponseDTO(
                category,//        Category category;
                sortBy,//        SortBy sortBy;
                page,//        Integer page;
                post.getSize(),//       Integer size;
                post.getTotalElements(),//        Integer totalElements;
                post.getTotalPages(),//        Integer totalPages;
                summaryDTOList//        List<PostResponseDTO.GgulPostSummaryDTO> posts;
        );
    }
}
