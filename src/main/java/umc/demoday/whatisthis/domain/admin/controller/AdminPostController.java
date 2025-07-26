package umc.demoday.whatisthis.domain.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.demoday.whatisthis.domain.admin.dto.AdminPostReqDTO;
import umc.demoday.whatisthis.domain.admin.dto.AdminPostResDTO;
import umc.demoday.whatisthis.domain.admin.service.AdminPostService;
import umc.demoday.whatisthis.global.apiPayload.CustomResponse;

@RestController
@RequestMapping("/admin/posts")
@RequiredArgsConstructor
public class AdminPostController {

    private final AdminPostService adminPostService;

    @GetMapping("/{post-id}")
    public CustomResponse<AdminPostResDTO> getPost(@PathVariable("post-id") Integer postId){
        AdminPostResDTO response = adminPostService.getPost(postId);
        return CustomResponse.ok(response);
    }

    @DeleteMapping("/{post-id}")
    public CustomResponse<Integer> deletePost(@PathVariable("post-id") Integer postId){
        adminPostService.deletePost(postId);
        return CustomResponse.ok(postId);
    }

    @PatchMapping("/{post-id}")
    public CustomResponse<AdminPostResDTO.updatePostResDTO> updatePost(@PathVariable("post-id") Integer postId, @RequestBody AdminPostReqDTO.updatePostReqDTO request){
        AdminPostResDTO.updatePostResDTO response = adminPostService.updatePost(postId, request);
        return CustomResponse.ok(response);
    }
}
