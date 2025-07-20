package umc.demoday.whatisthis.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.member.service.member.MemberCommandService;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.dto.PostRequestDTO;
import umc.demoday.whatisthis.domain.post.dto.PostResponseDTO;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.domain.post.enums.SortBy;
import umc.demoday.whatisthis.domain.post.service.PostService;
import umc.demoday.whatisthis.global.apiPayload.CustomResponse;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralSuccessCode;

import java.util.Set;
import java.util.stream.Collectors;

import static umc.demoday.whatisthis.domain.post.converter.PostConverter.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final MemberCommandService memberCommandService;

    @GetMapping("/communities")
    @Operation(summary = "커뮤니티 페이지 조회 API (전체) -by 남성현")
    public CustomResponse<PostResponseDTO.CommunityPostPreviewListDTO> communityList
            (@Parameter(description = "페이지 번호") @RequestParam Integer page,
             @Parameter(description = "한 페이지 당 게시물 수")@RequestParam Integer size,
             @Parameter(description = "인기순 = BEST ,최신순 = LATEST ") @RequestParam SortBy sort) {

        Page<Post> postList = postService.getAllPosts(page, size, sort);

        Set<Member> members = postList.stream()
                .map(Post::getMember)
                .collect(Collectors.toSet());

        for (Member member : members) {
            memberCommandService.evaluateIsBest(member);
        }

        return CustomResponse.onSuccess(GeneralSuccessCode.OK,toCommunityPostPreviewListDTO(postList));
    }

    @GetMapping("/communities/popular")
    @Operation(summary = "커뮤니티 페이지 조회 API (인기 글) -by 남성현")
    public CustomResponse<PostResponseDTO.CommunityPostPreviewListDTO> popCommunityList
            (@Parameter(description = "페이지 번호") @RequestParam Integer page,
             @Parameter(description = "한 페이지 당 게시물 수")@RequestParam Integer size) {

        Page<Post> postList = postService.getBestPosts(page, size);

        Set<Member> members = postList.stream()
                .map(Post::getMember)
                .collect(Collectors.toSet());

        for (Member member : members) {
            memberCommandService.evaluateIsBest(member);
        }
        return CustomResponse.onSuccess(GeneralSuccessCode.OK,toCommunityPostPreviewListDTO(postList));
    }

    @GetMapping("/communities/tips")
    @Operation(summary = "커뮤니티 페이지 조회 API (생활 꿀팁) -by 남성현")
    public CustomResponse<PostResponseDTO.CommunityPostPreviewListDTO> tipCommunityList
            (@Parameter(description = "페이지 번호") @RequestParam Integer page,
             @Parameter(description = "한 페이지 당 게시물 수")@RequestParam Integer size,
             @Parameter(description = "인기순 = BEST ,최신순 = LATEST ") @RequestParam SortBy sort) {

        Page<Post> postList = postService.getAllPostsByCategory(page, size, sort, Category.TIP);

        Set<Member> members = postList.stream()
                .map(Post::getMember)
                .collect(Collectors.toSet());

        for (Member member : members) {
            memberCommandService.evaluateIsBest(member);
        }

        return CustomResponse.onSuccess(GeneralSuccessCode.OK,toCommunityPostPreviewListDTO(postList));
    }

    @GetMapping("/communities/items")
    @Operation(summary = "커뮤니티 페이지 조회 API (꿀템 추천) -by 남성현")
    public CustomResponse<PostResponseDTO.CommunityPostPreviewListDTO> itemCommunityList
            (@Parameter(description = "페이지 번호") @RequestParam Integer page,
             @Parameter(description = "한 페이지 당 게시물 수")@RequestParam Integer size,
             @Parameter(description = "인기순 = BEST ,최신순 = LATEST ") @RequestParam SortBy sort) {

        Page<Post> postList = postService.getAllPostsByCategory(page, size, sort, Category.ITEM);

        Set<Member> members = postList.stream()
                .map(Post::getMember)
                .collect(Collectors.toSet());

        for (Member member : members) {
            memberCommandService.evaluateIsBest(member);
        }

        return CustomResponse.onSuccess(GeneralSuccessCode.OK,toCommunityPostPreviewListDTO(postList));
    }

    @GetMapping("/communities/should-i-buy")
    @Operation(summary = "커뮤니티 페이지 조회 API (살까 말까?) -by 남성현")
    public CustomResponse<PostResponseDTO.CommunityPostPreviewListDTO> buyCommunityList
            (@Parameter(description = "페이지 번호") @RequestParam Integer page,
             @Parameter(description = "한 페이지 당 게시물 수")@RequestParam Integer size,
             @Parameter(description = "인기순 = BEST ,최신순 = LATEST ") @RequestParam SortBy sort) {

        Page<Post> postList = postService.getAllPostsByCategory(page, size, sort, Category.SHOULD_I_BUY);

        Set<Member> members = postList.stream()
                .map(Post::getMember)
                .collect(Collectors.toSet());

        for (Member member : members) {
            memberCommandService.evaluateIsBest(member);
        }

        return CustomResponse.onSuccess(GeneralSuccessCode.OK,toCommunityPostPreviewListDTO(postList));
    }

    @GetMapping("/communities/curious")
    @Operation(summary = "커뮤니티 페이지 조회 API (궁금해요!) -by 남성현")
    public CustomResponse<PostResponseDTO.CommunityPostPreviewListDTO> curiousCommunityList
            (@Parameter(description = "페이지 번호") @RequestParam Integer page,
             @Parameter(description = "한 페이지 당 게시물 수")@RequestParam Integer size,
             @Parameter(description = "인기순 = BEST ,최신순 = LATEST ") @RequestParam SortBy sort) {

        Page<Post> postList = postService.getAllPostsByCategory(page, size, sort, Category.CURIOUS);

        Set<Member> members = postList.stream()
                .map(Post::getMember)
                .collect(Collectors.toSet());

        for (Member member : members) {
            memberCommandService.evaluateIsBest(member);
        }

        return CustomResponse.onSuccess(GeneralSuccessCode.OK,toCommunityPostPreviewListDTO(postList));
    }

    @PostMapping
    @Operation(summary = "커뮤니티 글 작성 API -by 남성현")
    public CustomResponse<PostResponseDTO.NewPostResponseDTO> newPost
            (@RequestBody PostRequestDTO.NewPostRequestDTO request,
             @AuthenticationPrincipal Member loginUser) {

        Post newPost = postService.insertNewPost(toNewPost(request, loginUser));

        return CustomResponse.onSuccess(GeneralSuccessCode.CREATED,toNewPostDTO(newPost));
    }
}
