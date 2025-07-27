package umc.demoday.whatisthis.domain.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.demoday.whatisthis.domain.admin.dto.AdminPostReqDTO;
import umc.demoday.whatisthis.domain.admin.dto.AdminPostResDTO;
import umc.demoday.whatisthis.domain.admin.service.AdminPostService;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.domain.post.enums.SortBy;
import umc.demoday.whatisthis.global.apiPayload.CustomResponse;

import java.util.List;

@RestController
@RequestMapping("/admin/posts")
@RequiredArgsConstructor
public class AdminPostController {

    private final AdminPostService adminPostService;

    @GetMapping("/{post-id}")
    @Operation(summary = "개별 게시글 조회 API -by 천성호")
    public CustomResponse<AdminPostResDTO> getPost(@PathVariable("post-id") Integer postId){
        AdminPostResDTO response = adminPostService.getPost(postId);
        return CustomResponse.ok(response);
    }

    @DeleteMapping("/{post-id}")
    @Operation(summary = "게시글 삭제 API -by 천성호")
    public CustomResponse<Integer> deletePost(@PathVariable("post-id") Integer postId){
        adminPostService.deletePost(postId);
        return CustomResponse.ok(postId);
    }

    @PatchMapping("/{post-id}")
    @Operation(summary = "게시글 업데이트 API -by 천성호")
    public CustomResponse<AdminPostResDTO.updatePostResDTO> updatePost(@PathVariable("post-id") Integer postId, @RequestBody AdminPostReqDTO.updatePostReqDTO request){
        AdminPostResDTO.updatePostResDTO response = adminPostService.updatePost(postId, request);
        return CustomResponse.ok(response);
    }

    @PostMapping("/")
    @Operation(summary = "게시글 작성 API -by 천성호")
    public CustomResponse<AdminPostResDTO.createPostResDTO> createPost(@RequestBody AdminPostReqDTO.createPostReqDTO request){
        AdminPostResDTO.createPostResDTO response = adminPostService.createPost(request);
        return CustomResponse.ok(response);
    }

    @GetMapping("/")
    @Operation(summary = "게시물 목록 조회 API -by 천성호")
    public CustomResponse<AdminPostResDTO.allPostResDTO> getAllPosts(@RequestParam(name = "category", required = false) Category category,
                                                                              @RequestParam("page") Integer page,
                                                                              @RequestParam("size") Integer size){

        AdminPostResDTO.allPostResDTO response = adminPostService.getAllPosts(category, page, size);
        return CustomResponse.ok(response);
    }
}
