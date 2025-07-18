package umc.demoday.whatisthis.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.dto.PostResponseDTO;
import umc.demoday.whatisthis.domain.post.enums.SortBy;
import umc.demoday.whatisthis.domain.post.service.PostService;
import umc.demoday.whatisthis.global.apiPayload.CustomResponse;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralSuccessCode;

import static umc.demoday.whatisthis.domain.post.converter.PostConverter.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @GetMapping("/communities")
    @Operation(summary = "커뮤니티 페이지 조회 API (전체) -by 남성현")
    public CustomResponse<PostResponseDTO.CommunityPostPreviewListDTO> communityList
            (@Parameter(description = "페이지 번호") @RequestParam Integer page,
             @Parameter(description = "한 페이지 당 게시물 수")@RequestParam Integer size,
             @Parameter(description = "인기순 = BEST ,최신순 = LATEST ") @RequestParam SortBy sort) {

        Page<Post> postList = postService.getAllPosts(page, size, sort);

        return CustomResponse.onSuccess(GeneralSuccessCode.OK,toCommunityPostPreviewListDTO(postList));
    }

    @GetMapping("/communities/popular")
    @Operation(summary = "커뮤니티 페이지 조회 API (인기 글) -by 남성현")
    public CustomResponse<PostResponseDTO.CommunityPostPreviewListDTO> popCommunityList
            (@Parameter(description = "페이지 번호") @RequestParam Integer page,
             @Parameter(description = "한 페이지 당 게시물 수")@RequestParam Integer size) {

        Page<Post> postList = postService.getBestPosts(page, size);

        return CustomResponse.onSuccess(GeneralSuccessCode.OK,toCommunityPostPreviewListDTO(postList));
    }






}
