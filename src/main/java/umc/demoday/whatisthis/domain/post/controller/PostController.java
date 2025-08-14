package umc.demoday.whatisthis.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import umc.demoday.whatisthis.domain.admin.redis.PostDocument;
import umc.demoday.whatisthis.domain.admin.redis.async.InitialDataIndexer;
import umc.demoday.whatisthis.domain.admin.redis.search.SearchService;
import umc.demoday.whatisthis.domain.member_profile.MemberActivityService;
import umc.demoday.whatisthis.domain.post.dto.IntegratedSearchResponseDTO;
import umc.demoday.whatisthis.domain.post.dto.MainPageResponseDTO;
import umc.demoday.whatisthis.domain.post.dto.PostResponseDTO;
import umc.demoday.whatisthis.domain.post.dto.SingleSearchDTO;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.domain.post.enums.SortBy;
import umc.demoday.whatisthis.domain.post.service.PostService;;
import umc.demoday.whatisthis.global.CustomUserDetails;
import umc.demoday.whatisthis.global.apiPayload.CustomResponse;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralSuccessCode;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final MemberActivityService memberActivityService;
    private final SearchService searchService;
    private final InitialDataIndexer initialDataIndexer;

    @GetMapping("/{post-id}")
    @Operation(summary = "생활꿀팁 or 생활꿀팁 페이지 조회 API -by 천성호")
    public CustomResponse<PostResponseDTO.GgulPostResponseDTO> getGgulPost(@PathVariable("post-id") Integer postId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        PostResponseDTO.GgulPostResponseDTO result = postService.getGgulPost(postId,customUserDetails);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK,result);
    }


    @PostMapping("/{post-id}/scraps")
    @Operation(summary = "게시물 스크랩 API - by 천성호")
    public CustomResponse<Void> scrapPost (@PathVariable("post-id") Integer postId, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        postService.scrapPost(postId,customUserDetails);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, null);
    }

    @DeleteMapping("/{post-id}/scraps/{scrap-id}")
    @Operation(summary = "스크랩 삭제 API - by 천성호")
    public CustomResponse<Void> deleteScrap (@PathVariable("scrap-id") Integer scrapId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        postService.deleteScrap(scrapId, customUserDetails);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, null);
    }

    @GetMapping("/life-tips")
    @Operation(summary = "생활 꿀팁 카테고리 별 게시글 목록 조회 API - by 천성호" )
    public CustomResponse<PostResponseDTO.GgulPostsByCategoryResponseDTO> getGgulTipPostsByCategory(@RequestParam("category") Category category,
                                                                               @RequestParam("sort") SortBy sort,
                                                                               @RequestParam("page") Integer page,
                                                                               @RequestParam("size") Integer size ){

        PostResponseDTO.GgulPostsByCategoryResponseDTO result = postService.getGgulPostsByCategory(category,sort,page,size);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, result);
    }
    @GetMapping("/life-items")
    @Operation(summary = "생활 꿀템 카테고리 별 게시글 목록 조회 API - by 천성호" )
    public CustomResponse<PostResponseDTO.GgulPostsByCategoryResponseDTO> getGgulItemPostsByCategory(@RequestParam("category") Category category,
                                                                                                    @RequestParam("sort") SortBy sort,
                                                                                                    @RequestParam("page") Integer page,
                                                                                                    @RequestParam("size") Integer size ){

        PostResponseDTO.GgulPostsByCategoryResponseDTO result = postService.getGgulPostsByCategory(category,sort,page,size);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, result);
    }
    @GetMapping("/life-tips/ai")
    @Operation(summary = "생활 꿀팁 AI 추천 게시글 목록 조회 API - by 천성호" )
    public CustomResponse<PostResponseDTO.GgulPostsByAiResponseDTO> getGgulTipsPostsByAi(@RequestParam("page") Integer page,
                                                                                               @RequestParam("size") Integer size,
                                                                                               @AuthenticationPrincipal CustomUserDetails customUserDetails){

        PostResponseDTO.GgulPostsByAiResponseDTO result = postService.getPostsByAiRecommendation(customUserDetails,page,size,Category.LIFE_TIP);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, result);
    }
    @GetMapping("/life-items/ai")
    @Operation(summary = "생활 꿀템 AI 추천 게시글 목록 조회 API - by 천성호" )
    public CustomResponse<PostResponseDTO.GgulPostsByAiResponseDTO> getGgulItemPostsByAi(@RequestParam("page") Integer page,
                                                                                         @RequestParam("size") Integer size,
                                                                                         @AuthenticationPrincipal CustomUserDetails customUserDetails){

        PostResponseDTO.GgulPostsByAiResponseDTO result = postService.getPostsByAiRecommendation(customUserDetails,page,size,Category.LIFE_ITEM);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, result);
    }
    @GetMapping("/life-tips/all")
    @Operation(summary = "생활 꿀팁 전체 페이지 API - by 천성호")
    public CustomResponse<MainPageResponseDTO> getAllGgulTipPosts(@RequestParam("page") Integer page,@AuthenticationPrincipal CustomUserDetails customUserDetails){
        MainPageResponseDTO result = postService.getAllGgulPosts(Category.LIFE_TIP, page, 6, customUserDetails);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, result);
    }

    @GetMapping("/life-items/all")
    @Operation(summary = "생활 꿀템 전체 페이지 API - by 천성호")
    public CustomResponse<MainPageResponseDTO> getAllGgulItemPosts(@RequestParam("page") Integer page, @AuthenticationPrincipal CustomUserDetails customUserDetails){

        MainPageResponseDTO result = postService.getAllGgulPosts(Category.LIFE_ITEM, page, 6, customUserDetails);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, result);
    }

    // 사용자가 게시물을 조회했음을 기록하는 API
    @PostMapping("/{postId}/view-history")
    @Operation(summary = "최근 조회한 게시물로 기록하는 API - by 천성호")
    public CustomResponse<Void> recordPostView(@PathVariable Integer postId, @AuthenticationPrincipal CustomUserDetails customUserDetails)
    {
        // JWT에서 사용자 ID 추출
        memberActivityService.updateLastSeenPost(customUserDetails, postId);
        return CustomResponse.ok(null);
    }

    @GetMapping("/popular_keywords")
    @Operation(summary = "인기 검색어 조회 API - by 천성호")
    public CustomResponse<List<String>> getPopularKeywords(){
        List<String> keywords = searchService.getPopularKeywords(10);
        return CustomResponse.ok(keywords);
    }

    @GetMapping("/search/single")
    @Operation(summary = "단일 카테고리 검색 API -by 천성호")
    public CustomResponse<SingleSearchDTO> search(@RequestParam("keyword") String keyword, @RequestParam(defaultValue = "TIP") Category category, @RequestParam Integer page, @RequestParam Integer size){
        Page<PostDocument> posts = searchService.executeSearch(keyword, category, page+1, size);
        List<SingleSearchDTO.SummaryDTO> postList = posts.stream().map(SingleSearchDTO::toSummaryDTO).toList();
        return CustomResponse.ok(SingleSearchDTO.builder().results(postList).build());
    }

    @GetMapping("/search")
    @Operation(summary = "통합 검색 API -by 천성호")
    public CustomResponse<IntegratedSearchResponseDTO> integratedSearch(@RequestParam("keyword") String keyword)
    {
        Page<PostDocument> tipSearch = searchService.executeSearch(keyword, Category.LIFE_TIP, 1, 5);
        Page<PostDocument> itemSearch = searchService.executeSearch(keyword, Category.LIFE_ITEM, 1, 5);
        Page<PostDocument> communitySearch = searchService.executeSearch(keyword, Category.ITEM, 1, 5);
        IntegratedSearchResponseDTO integratedSearchResponseDTO = IntegratedSearchResponseDTO.toIntegratedSearchResponseDTO(
                tipSearch,
                itemSearch,
                communitySearch
        );
        return CustomResponse.ok(integratedSearchResponseDTO);
    }

    @GetMapping("/{postId}/similar")
    @Operation(summary = "관련 포스트 추천 API -by 천성호")
    public CustomResponse<List<PostResponseDTO.PostSummaryDTO>> getSimilarPost(@PathVariable Integer postId, @RequestParam Integer size)
    {
        List<PostResponseDTO.PostSummaryDTO> response = postService.getSimilarPost(postId, size);
        return CustomResponse.ok(response);
    }
}
