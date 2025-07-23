package umc.demoday.whatisthis.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.dto.MainPageResponseDTO;
import umc.demoday.whatisthis.domain.post.dto.PostResponseDTO;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.domain.post.enums.SortBy;
import umc.demoday.whatisthis.domain.post.service.PostService;
import umc.demoday.whatisthis.domain.post.service.PostServiceImpl;
import umc.demoday.whatisthis.global.apiPayload.CustomResponse;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralSuccessCode;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @GetMapping("/{post-id}")
    @Operation(summary = "생활꿀팁 or 생활꿀팁 페이지 조회 API -by 천성호")
    public CustomResponse<PostResponseDTO.GgulPostResponseDTO> getGgulPost(@PathVariable("post-id") Integer postId){

        PostResponseDTO.GgulPostResponseDTO result = postService.getGgulPost(postId);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK,result);
    }


    @PostMapping("/{post-id}/scraps")
    @Operation(summary = "게시물 스크랩 API - by 천성호")
    public CustomResponse<Void> scrapPost (@PathVariable("post-id") Integer postId){
        postService.scrapPost(postId);
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

    @GetMapping("/life-tips/all")
    @Operation(summary = "생활 꿀팁 전체 페이지 API - by 천성호")
    public CustomResponse<MainPageResponseDTO> getAllGgulTipPosts(@RequestParam("page") Integer page){
        MainPageResponseDTO result = postService.getAllGgulPosts(Category.LIFE_TIP, page, 6);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, result);
    }

    @GetMapping("/life-items/all")
    @Operation(summary = "생활 꿀템 전체 페이지 API - by 천성호")
    public CustomResponse<MainPageResponseDTO> getAllGgulItemPosts(@RequestParam("page") Integer page){

        MainPageResponseDTO result = postService.getAllGgulPosts(Category.LIFE_ITEM, page, 6);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, result);
    }
}
