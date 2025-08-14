package umc.demoday.whatisthis.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.demoday.whatisthis.domain.hashtag.Hashtag;
import umc.demoday.whatisthis.domain.hashtag.repository.HashtagRepository;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.member.repository.MemberRepository;
import umc.demoday.whatisthis.domain.member_profile.MemberActivityService;
import umc.demoday.whatisthis.domain.member_profile.MemberProfileRepository;
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
import umc.demoday.whatisthis.domain.recommendation.RecommendationService;
import umc.demoday.whatisthis.global.CustomUserDetails;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;

import java.util.*;

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
    private final MemberProfileRepository memberProfileRepository;
    private final RecommendationService recommendationService;
    private final MemberActivityService memberActivityService;

    @Override
    public PostResponseDTO.GgulPostResponseDTO getGgulPost(Integer postId, CustomUserDetails customUserDetails) {
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

        // 최근 조회한 게시글 갱신
        if (customUserDetails != null && customUserDetails.getRole().equals("ROLE_USER")) //유저면
            memberActivityService.updateLastSeenPost(customUserDetails, postId);

        //viewcount 증가
        post.setViewCount(post.getViewCount() + 1);

        // 3. 모든 데이터를 조합하여 최종 DTO 생성 후 반환
        return PostConverter.toGgulPostResponseDTO(post,category,imageUrls,hashtags,postScrapCount);
    }

    public void scrapPost(Integer postId, CustomUserDetails customUserDetails) {

        // Post 불러오기
        Post post = postRepository.findById(postId).orElseThrow(() -> new GeneralException(GeneralErrorCode.NOT_FOUND_404));


        // Member 불러와서 스크랩
        if(customUserDetails != null && customUserDetails.getRole().equals("ROLE_USER")) {

            Member member = memberRepository.findById(customUserDetails.getId())
                    .orElseThrow(() -> new GeneralException(GeneralErrorCode.NOT_FOUND_404));


            if (postScrapRepository.existsByMemberAndPost(member, post)) {
                throw new GeneralException(GeneralErrorCode.ALREADY_EXIST_MEMBER_ID, "이미 스크랩한 게시물입니다.");
            }
            PostScrap postScrap = new PostScrap(member, post);
            postScrapRepository.save(postScrap);

        }
        else throw new GeneralException(GeneralErrorCode.FORBIDDEN_403, "스크랩 권한이 없습니다.");
    }


    public void deleteScrap(Integer scrapId,CustomUserDetails customUserDetails) {
        if(customUserDetails.getRole().equals("ROLE_USER")) {
            // 현재 멤버의 ID 찾기
            Integer currentMemberId = customUserDetails.getId();

            // PostScrap 찾기
            PostScrap postScrap = postScrapRepository.findById(scrapId)
                    .orElseThrow(() -> new GeneralException(GeneralErrorCode.NOT_FOUND_404));

            // 스크랩 소유자가 현재 멤버인지 검증하기
            if (!postScrap.getMember().getId().equals(currentMemberId)) {
                throw new GeneralException(GeneralErrorCode.FORBIDDEN_403); // 권한 없음 예외
            }

            // 삭제하기
            postScrapRepository.delete(postScrap);
        }
        else throw new GeneralException(GeneralErrorCode.FORBIDDEN_403, "스크랩 권한이 없습니다.");
    }
    @Override
    public PostResponseDTO.GgulPostsByCategoryResponseDTO getGgulPostsByCategory(Category category, SortBy sort, Integer page, Integer size) {
        // 1. 정렬 기준(Sort) 객체 생성
        Sort sortKey = null;
        if ("BEST".equalsIgnoreCase(sort.toString())) {
            // BEST(인기순) -> likeCount(좋아요 수)가 높은 순서대로 정렬
            sortKey = Sort.by(Sort.Direction.DESC, "likeCount");
        } else if("LATEST".equalsIgnoreCase(sort.toString())) {
            // LATEST(최신순) -> createdAt(생성일)이 최신인 순서대로 정렬
            sortKey = Sort.by(Sort.Direction.DESC, "createdAt");
        }

        // 2. 페이지 요청(Pageable) 객체 생성
        Pageable pageable = PageRequest.of(page, size, Objects.requireNonNull(sortKey));

        // 3. Repository를 통해 데이터베이스에서 데이터 조회
        Page<Post> postPage = postRepository.findByCategory(category, pageable);

        // 4. 조회된 Post 엔티티를 GgulPostsByCategoryResponseDTO 로 변환
        return pageConverter.toGgulPostsByCategoryResponseDTO(postPage,category,sort);
    }

    @Override
    public PostResponseDTO.GgulPostsByAiResponseDTO  getPostsByAiRecommendation(CustomUserDetails customUserDetails, Integer page, Integer size, Category category) {
        List<Integer> allRecommendedList = recommendationService.findRecommendationsForMember(customUserDetails,size,category);

        //원하는 페이지만큼 짜르기
        List<Post> posts = postRepository.findAllById(allRecommendedList.subList(page * size, (page + 1) * size));
        Map<Integer, String> aiPostThumbnailsMap = pageConverter.findThumbnails(allRecommendedList);
        Map<Integer, List<Hashtag>> aiPostHashtagsMap = pageConverter.findHashtags(allRecommendedList);
        Map<Integer, Integer> aiPostScrapCountsMap = pageConverter.getScrapCountMap(allRecommendedList);

        List<PostResponseDTO.PostSummaryDTO> summaryDTOS = posts.stream().map(post -> pageConverter.toGgulPostSummaryDTO(
                post,
                aiPostThumbnailsMap.get(post.getId()),
                aiPostHashtagsMap.getOrDefault(post.getId(), Collections.emptyList()),
                aiPostScrapCountsMap.getOrDefault(post.getId(), 0)
                ))
                .toList();

        return new PostResponseDTO.GgulPostsByAiResponseDTO(
                SortBy.AI,
                page,
                size,
                ((long) (page + 1) * size),
                page,
                summaryDTOS
        );
    }
    @Override
    public MainPageResponseDTO getAllGgulPosts(Category category, Integer page, Integer size, CustomUserDetails customUserDetails) {

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
        else throw new IllegalArgumentException("지원하지 않는 카테고리입니다: " + category);

        // 2. Best 정렬 페이지 요청(Pageable) 객체 생성
        Pageable pageableBest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "likeCount"));

        // 3. Latest 정렬 페이지 요청(Pageable) 객체 생성
        Pageable pageableLatest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        // 4. 추천 게시물 리스트
        List<Integer> recommendedPostIds = recommendationService.findRecommendationsForMember(customUserDetails,size,category);

        // 5. Repository를 통해 데이터베이스에서 데이터 조회
        Page<Post> bestPostPage = postRepository.findByCategoryIn(categoryList, pageableBest);
        Page<Post> latestPostPage = postRepository.findByCategoryIn(categoryList, pageableLatest);
        // 6. 조회된 Post 엔티티를 MainPageResponseDTO 로 변환
        return pageConverter.toMainPageResponseDTO(bestPostPage, latestPostPage, recommendedPostIds, categoryList, category);


    }

    @Override
    public List<PostResponseDTO.PostSummaryDTO> getSimilarPost(Integer postId, Integer size){
        Post postForRecommendation = postRepository.findById(postId).orElseThrow(() -> new GeneralException(GeneralErrorCode.NOT_FOUND_404));

        Category category = Category.LIFE_ITEM;
        if(postForRecommendation.getCategory().toString().endsWith("_TIP") )
            category = Category.LIFE_TIP;

        List<Integer> allRecommendedList = recommendationService.findSimilarPostsInVectorDB(postId.toString(), size, category);

        List<Post> posts = postRepository.findAllById(allRecommendedList);

        Map<Integer, String> aiPostThumbnailsMap = pageConverter.findThumbnails(allRecommendedList);
        Map<Integer, List<Hashtag>> aiPostHashtagsMap = pageConverter.findHashtags(allRecommendedList);
        Map<Integer, Integer> aiPostScrapCountsMap = pageConverter.getScrapCountMap(allRecommendedList);

        return posts.stream().map(post -> pageConverter.toGgulPostSummaryDTO(
                        post,
                        aiPostThumbnailsMap.get(post.getId()),
                        aiPostHashtagsMap.getOrDefault(post.getId(), Collections.emptyList()),
                        aiPostScrapCountsMap.getOrDefault(post.getId(), 0)
                ))
                .toList();
    }

}

