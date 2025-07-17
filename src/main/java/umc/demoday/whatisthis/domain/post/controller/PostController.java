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

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postServiceImpl;

    @GetMapping("/{post-id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "생활꿀팁 or 생활꿀팁 페이지 조회 API -by 천성호")
    public CustomResponse<PostResponseDTO.GgulPostResponseDTO> getGgulPost(@PathVariable Long postId){

        PostResponseDTO.GgulPostResponseDTO response = PostServiceImpl.getGgulPost(postId);
        return CustomResponse.onSuccess(response);
    }
}
