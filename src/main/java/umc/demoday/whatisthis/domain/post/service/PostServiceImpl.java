package umc.demoday.whatisthis.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.demoday.whatisthis.domain.hashtag.Hashtag;
import umc.demoday.whatisthis.domain.hashtag.repository.HashtagRepository;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.member.repository.MemberRepository;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.converter.PageConverter;
import umc.demoday.whatisthis.domain.post.converter.PostConverter;
import umc.demoday.whatisthis.domain.post.dto.MainPageResponseDTO;
import umc.demoday.whatisthis.domain.post.dto.PostResponseDTO;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.domain.post.enums.SortBy;
import umc.demoday.whatisthis.domain.post.repository.PostRepository;
import umc.demoday.whatisthis.domain.post_image.PostImage;
import umc.demoday.whatisthis.domain.post_image.repository.PostImageRepository;
import umc.demoday.whatisthis.domain.post_scrap.PostScrap;
import umc.demoday.whatisthis.domain.post_scrap.repository.PostScrapRepository;
import umc.demoday.whatisthis.global.CustomUserDetails;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final PostScrapRepository postScrapRepository;
    private final HashtagRepository hashtagRepository;
    private final MemberRepository memberRepository;
    private final PageConverter pageConverter;
  
    @Override
    public PostResponseDTO.GgulPostResponseDTO getGgulPost(Integer postId) {
        // 1. 게시글 정보 조회
        Post post = postRepository.findById(postId).orElseThrow(() -> new GeneralException(GeneralErrorCode.NOT_FOUND_404));

        //Post.category 기준으로 DTO category(e. g. 생활 꿀팁) 설정
        String category = "생활꿀템";
        if(post.getCategory().name().endsWith("_TIP"))
            category = "생활꿀팁";

        // 2. 추가 정보들을 각 Repository에서 조회
        // 2-1. 이미지 목록 조회
        List<String> imageUrls = postImageRepository.findAllByPostId(postId).stream()
                .map(PostImage::getImageUrl)
                .toList();

        // 2-2. 해시태그 목록 조회
        List<String> hashtags = hashtagRepository.findAllByPostId(postId).stream()
                .map(Hashtag::getContent)
                .toList();

        // 2-3. 스크랩 수 조회
        int postScrapCount = postScrapRepository.countByPostId(postId);

        // 3. 모든 데이터를 조합하여 최종 DTO 생성 후 반환
        return PostConverter.toGgulPostResponseDTO(post,category,imageUrls,hashtags,postScrapCount);
    }

    public void scrapPost(Integer postId) {

        // Post 불러오기
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) throw new GeneralException(GeneralErrorCode.NOT_FOUND_404);

        // 멤버 ID 불러오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            Integer memberId = userDetails.getId();
            // 멤버 엔티티 불러오기
            Member member = memberRepository.findById(memberId).orElseThrow();
            PostScrap postScrap = new PostScrap(member, post);
            postScrapRepository.save(postScrap);
        }
    }

    @Override
    public PostResponseDTO.GgulPostsByCategoryResponseDTO getGgulPostsByCategory(Category category, SortBy sort, Integer page, Integer size) {
        // 1. 정렬 기준(Sort) 객체 생성
        Sort sortKey;
        if ("BEST".equalsIgnoreCase(sort.toString())) {
            // BEST(인기순) -> likeCount(좋아요 수)가 높은 순서대로 정렬
            sortKey = Sort.by(Sort.Direction.DESC, "likeCount");
        } else {
            // LATEST(최신순) -> createdAt(생성일)이 최신인 순서대로 정렬
            sortKey = Sort.by(Sort.Direction.DESC, "createdAt");
        }

        // 2. 페이지 요청(Pageable) 객체 생성
        Pageable pageable = PageRequest.of(page, size, sortKey);

        // 3. Repository를 통해 데이터베이스에서 데이터 조회
        Page<Post> postPage = postRepository.findByCategory(category, pageable);

        // 4. 조회된 Post 엔티티를 GgulPostsByCategoryResponseDTO 로 변환
        return pageConverter.toGgulPostsByCategoryResponseDTO(postPage,category,sort);
    }

    @Override
    public MainPageResponseDTO getAllGgulPosts(Category category, Integer page, Integer size){
        // 1. category Enum List 생성
        List<Category> categoryList = List.of();
        if(category == Category.LIFE_TIP)
            categoryList = Arrays.stream(Category.values())
                .filter(ct -> ct.name().endsWith("_TIP"))
                .toList();
        else if(category == Category.LIFE_ITEM)
            categoryList = Arrays.stream(Category.values())
                    .filter(ct -> ct.name().endsWith("_ITEM"))
                    .toList();

        // 2. Best 정렬 페이지 요청(Pageable) 객체 생성
        Pageable pageableBest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "likeCount"));

        // 3. Latest 정렬 페이지 요청(Pageable) 객체 생성
        Pageable pageableLatest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        // 3. Repository를 통해 데이터베이스에서 데이터 조회
        Page<Post> bestPostPage = postRepository.findByCategoryIn(categoryList, pageableBest);
        Page<Post> latestPostPage = postRepository.findByCategoryIn(categoryList, pageableLatest);

        //  4. 조회된 Post 엔티티를 MainPageResponseDTO 로 변환
        return pageConverter.toMainPageResponseDTO(bestPostPage, latestPostPage, categoryList);
    }
}

