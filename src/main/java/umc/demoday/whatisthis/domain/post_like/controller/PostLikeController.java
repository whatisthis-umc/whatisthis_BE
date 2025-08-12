package umc.demoday.whatisthis.domain.post_like.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.member.repository.MemberRepository;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.dto.PostResponseDTO;
import umc.demoday.whatisthis.domain.post.enums.SortBy;
import umc.demoday.whatisthis.domain.post_like.service.PostLikeQueryService;
import umc.demoday.whatisthis.global.CustomUserDetails;
import umc.demoday.whatisthis.global.apiPayload.CustomResponse;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralSuccessCode;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;

import java.util.Set;
import java.util.stream.Collectors;

import static umc.demoday.whatisthis.domain.post.converter.PostConverter.toCommunityPostPreviewListDTO;
import static umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode.MEMBER_NOT_FOUND;

@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeQueryService postLikeQueryService;
    private final MemberRepository memberRepository;

    @GetMapping
    @Operation(summary = "좋아요 목록 기본 : 5개씩 -by 윤영석")
    public CustomResponse<PostResponseDTO.CommunityPostPreviewListDTO> getMyLikeList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @AuthenticationPrincipal CustomUserDetails user) {

        Member loginUser = memberRepository.findById(user.getId())
                .orElseThrow(() -> new GeneralException(MEMBER_NOT_FOUND));

        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size, Sort.by(Sort.Direction.DESC, "post.createdAt"));

        PostResponseDTO.CommunityPostPreviewListDTO myLikePostList = postLikeQueryService.getMyPostLikeList(loginUser, pageable);

        return CustomResponse.onSuccess(GeneralSuccessCode.OK, myLikePostList);
    }
}



