package umc.demoday.whatisthis.domain.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
