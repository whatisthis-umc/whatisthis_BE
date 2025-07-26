package umc.demoday.whatisthis.domain.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.demoday.whatisthis.domain.admin.dto.AdminPostResDTO;
import umc.demoday.whatisthis.domain.hashtag.Hashtag;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.domain.post.repository.PostRepository;
import umc.demoday.whatisthis.domain.post_image.PostImage;
import umc.demoday.whatisthis.domain.post_image.repository.PostImageRepository;
import umc.demoday.whatisthis.domain.post_scrap.repository.PostScrapRepository;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminPostService {

    private final PostRepository postRepository;
    private final PostScrapRepository postScrapRepository;


    public AdminPostResDTO getPost(Integer postId) {

        Post post = postRepository.findById(postId).orElseThrow(() -> new GeneralException(GeneralErrorCode.NOT_FOUND_404));

        //Post.category 기준으로 DTO category(e. g. 생활 꿀팁) 설정
        Category category = Category.LIFE_ITEM;
        if(post.getCategory().name().endsWith("_TIP"))
            category = Category.LIFE_TIP;

        // 이미지 링크들
        List<String> imageUrls = post.getPostImageList().stream()
                .map(PostImage::getImageUrl)
                .toList();

        // 스크랩 수 조회
        int postScrapCount = postScrapRepository.countByPostId(postId);

        return AdminPostResDTO.builder()
                .postId(postId)
                .title(post.getTitle())
                .content(post.getContent())
                .category(category)
                .nickname(post.getMember().getNickname())
                .createdAt(post.getCreatedAt())
                .viewCount(post.getViewCount())
                .scrapCount(postScrapCount)
                .imageUrls(imageUrls)
                .build();
    }
}
