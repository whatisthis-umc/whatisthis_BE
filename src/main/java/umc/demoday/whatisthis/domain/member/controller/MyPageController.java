package umc.demoday.whatisthis.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.demoday.whatisthis.domain.inquiry.dto.resDTO.MyPageInquiryResponseDTO;
import umc.demoday.whatisthis.domain.member.dto.member.MyPageAccountDTO;
import umc.demoday.whatisthis.domain.post.dto.MyPagePostResponseDTO;
import umc.demoday.whatisthis.global.apiPayload.CustomResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my-page")
public class MyPageController {

    @GetMapping("/posts")
    @Operation(summary = "마이페이지 나의 작성내역 조회 API -by 남성현")
    public CustomResponse<MyPagePostResponseDTO.MyPostPageDTO> getMyPagePosts
            (@Parameter(description = "페이지 번호") @RequestParam Integer page,
             @Parameter(description = "한 페이지 당 게시물 수") @RequestParam Integer size) {
        return null;
    }

    @DeleteMapping("/posts")
    @Operation(summary = "마이페이지 나의 작성내역 삭제 API -by 남성현")
    public CustomResponse<Void> deleteMyPagePosts
            (@Parameter(description = "삭제 할 post id") @PathVariable(name = "post-id") Integer id) {
        return null;
    }

    @GetMapping("/inquiries")
    @Operation(summary = "마이페이지 나의 문의내역 조회 API -by 남성현")
    public CustomResponse<MyPageInquiryResponseDTO.MyInquiryPageDTO> getMyPageInquiries
            (@Parameter(description = "페이지 번호") @RequestParam Integer page,
             @Parameter(description = "한 페이지 당 문의글 수") @RequestParam Integer size) {
        return null;
    }

    @GetMapping("/inquiries")
    @Operation(summary = "마이페이지 나의 문의내역 상세조회 API -by 남성현")
    public CustomResponse<MyPageInquiryResponseDTO.myInquiryDetailDTO> getMyInquiryDetail
            (@Parameter(description = "조회 할 inquiry id") @PathVariable(name = "inquiry-id") Integer id) {
        return null;
    }

    @DeleteMapping("/inquiries")
    @Operation(summary = "마이페이지 나의 문의내역 삭제 API -by 남성현")
    public CustomResponse<Void> deleteMyPageInquiries
            (@Parameter(description = "삭제 할 inquiry id") @PathVariable(name = "inquiry-id") Integer id) {
        return null;
    }

    @GetMapping("/account")
    @Operation(summary = "마이페이지 계정 관리 페이지 조회 API -by 남성현")
    public CustomResponse<MyPageAccountDTO.MyPageAccountResponseDTO> getMyPageAccount() {
        return null;
    }

    @PatchMapping("/account")
    @Operation(summary = "마이페이지 계정 관리 페이지 수정 API -by 남성현")
    public CustomResponse<MyPageAccountDTO.MyPageAccountResponseDTO> patchMyPageAccount
            (@Valid @RequestBody MyPageAccountDTO.MyPageAccountRequestDTO request) {
        return null;
    }

}
