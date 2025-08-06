package umc.demoday.whatisthis.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import umc.demoday.whatisthis.domain.inquiry.Inquiry;
import umc.demoday.whatisthis.domain.inquiry.dto.resDTO.MyPageInquiryResponseDTO;
import umc.demoday.whatisthis.domain.inquiry.service.MyPageInquiryService;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.member.dto.member.MyPageAccountDTO;
import umc.demoday.whatisthis.domain.member.service.member.MemberCommandService;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.dto.MyPagePostResponseDTO;
import umc.demoday.whatisthis.domain.post.service.MyPagePostService;
import umc.demoday.whatisthis.global.apiPayload.CustomResponse;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralSuccessCode;

import static umc.demoday.whatisthis.domain.inquiry.converter.MyPageInquiryConverter.toMyInquiryDetailDTO;
import static umc.demoday.whatisthis.domain.inquiry.converter.MyPageInquiryConverter.toMyInquiryPageDTO;
import static umc.demoday.whatisthis.domain.member.converter.MemberConverter.toMyPageAccountModifyDTO;
import static umc.demoday.whatisthis.domain.member.converter.MemberConverter.toMyPageAccountResponseDTO;
import static umc.demoday.whatisthis.domain.post.converter.MyPagePostConverter.toMyPostPageDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my-page")
public class MyPageController {

    private final MyPagePostService myPagePostService;
    private final MyPageInquiryService myPageInquiryService;
    private final MemberCommandService memberCommandService;

    @GetMapping("/posts")
    @Operation(summary = "마이페이지 나의 작성내역 조회 API -by 남성현", security = @SecurityRequirement(name = "JWT TOKEN"))
    public CustomResponse<MyPagePostResponseDTO.MyPostPageDTO> getMyPagePosts
            (@Parameter(description = "페이지 번호") @RequestParam Integer page,
             @Parameter(description = "한 페이지 당 게시물 수") @RequestParam Integer size,
             @AuthenticationPrincipal Member loginUser) {

        Page<Post> postList = myPagePostService.getMyPosts(page-1, size, loginUser);

        return CustomResponse.ok(toMyPostPageDTO(postList));
    }

    @DeleteMapping("/posts/{post-id}")
    @Operation(summary = "마이페이지 나의 작성내역 삭제 API -by 남성현", security = @SecurityRequirement(name = "JWT TOKEN"))
    public CustomResponse<Void> deleteMyPagePosts
            (@Parameter(description = "삭제 할 post id") @PathVariable(name = "post-id") Integer id,
             @AuthenticationPrincipal Member loginUser) {

        myPagePostService.deletePost(id, loginUser);

        return CustomResponse.onSuccess(GeneralSuccessCode.NO_CONTENT_204, null);
    }

    @GetMapping("/inquiries")
    @Operation(summary = "마이페이지 나의 문의내역 조회 API -by 남성현", security = @SecurityRequirement(name = "JWT TOKEN"))
    public CustomResponse<MyPageInquiryResponseDTO.MyInquiryPageDTO> getMyPageInquiries
            (@Parameter(description = "페이지 번호") @RequestParam Integer page,
             @Parameter(description = "한 페이지 당 문의글 수") @RequestParam Integer size,
             @AuthenticationPrincipal Member loginUser) {

        Page<Inquiry> inquiryList = myPageInquiryService.getMyInquiries(page-1, size, loginUser);

        return CustomResponse.ok(toMyInquiryPageDTO(inquiryList));
    }

    @GetMapping("/inquiries/detail")
    @Operation(summary = "마이페이지 나의 문의내역 상세조회 API -by 남성현", security = @SecurityRequirement(name = "JWT TOKEN"))
    public CustomResponse<MyPageInquiryResponseDTO.MyInquiryDetailDTO> getMyInquiryDetail
            (@Parameter(description = "조회 할 inquiry id") @PathVariable(name = "inquiry-id") Integer id,
             @AuthenticationPrincipal Member loginUser) {

        Inquiry inquiry = myPageInquiryService.getInquiryByIdAndMember(id, loginUser);

        return CustomResponse.ok(toMyInquiryDetailDTO(inquiry));
    }

    @DeleteMapping("/inquiries/{inquiry-id}")
    @Operation(summary = "마이페이지 나의 문의내역 삭제 API -by 남성현", security = @SecurityRequirement(name = "JWT TOKEN"))
    public CustomResponse<Void> deleteMyPageInquiries
            (@Parameter(description = "삭제 할 inquiry id") @PathVariable(name = "inquiry-id") Integer id,
             @AuthenticationPrincipal Member loginUser) {

        myPageInquiryService.deleteInquiryById(id, loginUser);

        return CustomResponse.onSuccess(GeneralSuccessCode.NO_CONTENT_204, null);
    }

    @GetMapping("/account")
    @Operation(summary = "마이페이지 계정 관리 페이지 조회 API -by 남성현", security = @SecurityRequirement(name = "JWT TOKEN"))
    public CustomResponse<MyPageAccountDTO.MyPageAccountResponseDTO> getMyPageAccount(@AuthenticationPrincipal Member loginUser) {

        return CustomResponse.ok(toMyPageAccountResponseDTO(loginUser));
    }

    @PatchMapping("/account")
    @Operation(summary = "마이페이지 계정 관리 페이지 수정 API -by 남성현", security = @SecurityRequirement(name = "JWT TOKEN"))
    public CustomResponse<MyPageAccountDTO.MyPageAccountModifyDTO> patchMyPageAccount
            (@Valid @RequestBody MyPageAccountDTO.MyPageAccountRequestDTO request,
             @AuthenticationPrincipal Member loginUser) {

        Member member = memberCommandService.updateMember(request,loginUser);

        return CustomResponse.ok(toMyPageAccountModifyDTO(loginUser));
    }

}
