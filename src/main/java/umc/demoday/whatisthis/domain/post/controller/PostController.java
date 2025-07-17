package umc.demoday.whatisthis.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import umc.demoday.whatisthis.domain.post.dto.PostResponseDTO;
import umc.demoday.whatisthis.global.apiPayload.CustomResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    @GetMapping("/communities")
    @Operation(summary = "커뮤니티 페이지 조회 API (전체) -by 남성현" )
    public CustomResponse<PostResponseDTO.CommunityPostPreviewListDTO> communityList
            (@RequestParam Integer page,@RequestParam Integer size) {
        return null;
    }

    @GetMapping("/communities")
    @Operation(summary = "커뮤니티 페이지 조회 API (전체) -by 남성현" )
    public CustomResponse<PostResponseDTO.CommunityPostPreviewListDTO> communityList
            (@RequestParam Integer page,@RequestParam Integer size) {
        return null;
    }




}
