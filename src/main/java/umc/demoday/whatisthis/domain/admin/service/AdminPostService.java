package umc.demoday.whatisthis.domain.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.demoday.whatisthis.domain.admin.dto.AdminPostReqDTO;
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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminPostService {

    private final PostRepository postRepository;
    private final PostScrapRepository postScrapRepository;
    private final PostImageRepository postImageRepository;


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

    @Transactional
    public Integer deletePost(Integer postId) {
        // 삭제하려는 게시글이 존재하는지 먼저 확인
        Post postToDelete = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.NOT_FOUND_404));

        //존재하면 삭제
        postRepository.delete(postToDelete);

        return postId;
    }

    @Transactional
    public AdminPostResDTO.updatePostResDTO updatePost(Integer postId, AdminPostReqDTO.updatePostReqDTO request) {
        // 업데이트하려는 게시글이 존재하는지 먼저 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.NOT_FOUND_404));

        // 존재하면 업데이트
        if(request.getTitle() != null)
            post.setTitle(request.getTitle());

        if(request.getContent() != null)
            post.setContent(request.getContent());

        if(request.getCategory() != null)
            post.setCategory(request.getCategory());

        // 이미지 정보 업데이트
        if (request.getImageUrls() != null) {
            // 기존에 연관된 이미지를 모두 삭제
            postImageRepository.deleteAllByPost(post);

            // 새로운 이미지 URL 목록으로 PostImage 객체를 생성하고 저장.
            List<PostImage> newImages = request.getImageUrls().stream()
                    .map(imageUrl -> PostImage.builder().imageUrl(imageUrl).post(post).build())
                    .collect(Collectors.toList());
            postImageRepository.saveAll(newImages);

            // Post 엔티티에도 새로운 이미지 목록을 설정하여 상태를 동기화합니다.
            post.setPostImageList(newImages);
        }

        return AdminPostResDTO.updatePostResDTO.builder()
                .postId(post.getId())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
