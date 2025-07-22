package umc.demoday.whatisthis.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import umc.demoday.whatisthis.domain.post.dto.PostResponseDTO;
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

    @DeleteMapping("/{post-id}/scraps/{scrap-id}")
    @Operation(summary = "스크랩 삭제 API - by 천성호")
    public CustomResponse<Void> deleteScrap (@PathVariable("scrap-id") Integer scrapId){
        postService.deleteScrap(scrapId);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, null);
    }
}
