package umc.demoday.whatisthis.domain.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.demoday.whatisthis.domain.admin.dto.AdminPostReqDTO;
import umc.demoday.whatisthis.domain.admin.dto.AdminPostResDTO;
import umc.demoday.whatisthis.domain.hashtag.Hashtag;
import umc.demoday.whatisthis.domain.hashtag.repository.HashtagRepository;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.dto.PostResponseDTO;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.domain.post.enums.SortBy;
import umc.demoday.whatisthis.domain.post.repository.PostRepository;
import umc.demoday.whatisthis.domain.post.service.PostService;
import umc.demoday.whatisthis.domain.post.service.PostServiceImpl;
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
    private final HashtagRepository hashtagRepository;
    private final PostService postService;


    public AdminPostResDTO getPost(Integer postId) {

        Post post = postRepository.findById(postId).orElseThrow(() -> new GeneralException(GeneralErrorCode.NOT_FOUND_404));

        //Post.category 기준으로 DTO category(e. g. 생활 꿀팁) 설정
        Category category = Category.LIFE_ITEM;
        if (post.getCategory().name().endsWith("_TIP"))
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
                .subCategory(post.getCategory())
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
        if (request.getTitle() != null)
            post.setTitle(request.getTitle());

        if (request.getContent() != null)
            post.setContent(request.getContent());

        if (request.getSubCategory() != null)
            post.setCategory(request.getSubCategory());

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
        // 업데이트된 이미지 URL 목록을 가져오기
        List<String> updatedImageUrls = post.getPostImageList().stream()
                .map(PostImage::getImageUrl)
                .collect(Collectors.toList());


        // 해시태그 정보 업데이트
        if (request.getHashtags() != null)
        {
            // 기존에 연관된 해시태그 모두 삭제
            hashtagRepository.deleteAllByPost(post);

            // 새로운 해시태그 목록으로 Hashtag 객체를 생성하고 저장.
            List<Hashtag> newHashtags = request.getHashtags().stream()
                    .map(hashtag -> Hashtag.builder().content(hashtag).post(post).build())
                    .toList();
            hashtagRepository.saveAll(newHashtags);

            // Post 엔티티에도 새로운 해시태그 목록을 설정하여 상태를 동기화합니다.
            post.setHashtagList(newHashtags);
        }

        // 업데이트된 해시태그 목록을 가져오기
        List<String> updatedHashtags = post.getHashtagList().stream()
                .map(Hashtag::getContent)
                .toList();

        // 모든 필드를 채워서 반환
        return AdminPostResDTO.updatePostResDTO.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory())
                .imageUrls(updatedImageUrls)
                .hashtags(updatedHashtags)
                .updatedAt(post.getUpdatedAt())
                .build();
    }


    @Transactional
    public AdminPostResDTO.createPostResDTO createPost(AdminPostReqDTO.createPostReqDTO request) {

        Post newPost = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .category(request.getSubCategory())
                .likeCount(0)
                .viewCount(0)
                .build();

        // 카테고리 지정 잘했는지 확인
        if(request.getSubCategory().toString().endsWith("_TIP") && !request.getCategory().toString().equals("LIFE_TIP"))
            throw new IllegalStateException("올바른 카테고리가 아닙니다.");

        if(request.getSubCategory().toString().endsWith("_TEM") && !request.getCategory().toString().equals("LIFE_TEM"))
            throw new IllegalStateException("올바른 카테고리가 아닙니다.");

        //일단 게시글 생성
        postRepository.save(newPost);

        // ImageUrls -> List<PostImage> 타입으로 변경
        List<PostImage> images = request.getImageUrls().stream()
                .map(imageUrl -> PostImage.builder().imageUrl(imageUrl).post(newPost).build())
                .toList();

        // PostImageList setting
        newPost.setPostImageList(images);

        //response 용 savedImageUrls 생성
        List<String> savedImageUrls = newPost.getPostImageList().stream()
                .map(PostImage::getImageUrl)
                .toList();

        // Hashtag -> List<Hashtag> 타입으로 변경
        List<Hashtag> hashtags = request.getHashtags().stream()
                .map(hashtag -> Hashtag.builder().content(hashtag).post(newPost).build())
                .toList();

        // HashTags setting
        newPost.setHashtagList(hashtags);

        //response 용 savedHashtags 생성
        List<String> savedHashtags = newPost.getHashtagList().stream()
                .map(Hashtag::getContent)
                .toList();

        return AdminPostResDTO.createPostResDTO.builder()
                .postId(newPost.getId())
                .title(newPost.getTitle())
                .content(newPost.getContent())
                .category(request.getCategory())
                .subCategory(newPost.getCategory())
                .imageUrls(savedImageUrls)
                .hashtags(savedHashtags)
                .build();
    }

    public AdminPostResDTO.allPostResDTO getAllPosts(Category category, Integer page, Integer size) {
        PostResponseDTO.GgulPostsByCategoryResponseDTO ggulPostsByCategoryResponseDTO = postService.getGgulPostsByCategory(category, SortBy.LATEST, page, size);  //나중에는 requestParam에 따라서 category에 해당하는 게시글만 찾는 것으로 수정

        Category mainCategory;
        if(category.toString().endsWith("_TEM"))
            mainCategory = Category.LIFE_ITEM;
        else {
            mainCategory = Category.LIFE_TIP;
        }

        List<AdminPostResDTO.getAllPostResDTO> allPosts = ggulPostsByCategoryResponseDTO.getPosts().stream()
                .map(post -> AdminPostResDTO.getAllPostResDTO.builder()
                    .postId(post.getPostId())
                    .title(post.getTitle())
                    .content(post.getSummary())
                    .category(mainCategory).subCategory(category)
                    .createdAt(post.getCreatedAt())
                    .build())
                    .toList();

        return AdminPostResDTO.allPostResDTO.builder()
                .posts(allPosts)
                .page(ggulPostsByCategoryResponseDTO.getPage())
                .size(ggulPostsByCategoryResponseDTO.getSize())
                .totalPages(ggulPostsByCategoryResponseDTO.getTotalPages())
                .totalElements(ggulPostsByCategoryResponseDTO.getTotalElements())
                .build();

    }
}
