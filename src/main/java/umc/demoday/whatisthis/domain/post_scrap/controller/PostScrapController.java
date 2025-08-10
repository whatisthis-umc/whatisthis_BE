package umc.demoday.whatisthis.domain.post_scrap.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.post.dto.PostResponseDTO;
import umc.demoday.whatisthis.domain.post_scrap.dto.resDTO.MyScrapPageResDTO;
import umc.demoday.whatisthis.domain.post_scrap.service.PostScrapQueryService;
import umc.demoday.whatisthis.global.apiPayload.CustomResponse;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralSuccessCode;

@RestController
@RequestMapping("/scraps")
@RequiredArgsConstructor
public class PostScrapController {

    private final PostScrapQueryService postScrapQueryService;

    @GetMapping
    @Operation(summary = "스크랩 목록 기본 : 10개씩 -by 윤영석")
    public CustomResponse<MyScrapPageResDTO> getMyScrapList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal Member loginUser) {

        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size, Sort.by(Sort.Direction.DESC, "post.createdAt"));

        MyScrapPageResDTO myScrapPostList = postScrapQueryService.getMyPostScrapList(loginUser, pageable);

        return CustomResponse.onSuccess(GeneralSuccessCode.OK, myScrapPostList);
    }
}