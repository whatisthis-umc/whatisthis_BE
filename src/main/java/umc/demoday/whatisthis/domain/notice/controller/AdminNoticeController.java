package umc.demoday.whatisthis.domain.notice.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import umc.demoday.whatisthis.domain.notice.dto.reqDTO.NoticeCreateReqDTO;
import umc.demoday.whatisthis.domain.notice.dto.reqDTO.NoticeUpdateReqDTO;
import umc.demoday.whatisthis.domain.notice.dto.resDTO.NoticePageResDTO;
import umc.demoday.whatisthis.domain.notice.dto.resDTO.NoticeResDTO;
import umc.demoday.whatisthis.domain.notice.service.NoticeCommandService;
import umc.demoday.whatisthis.domain.notice.service.NoticeQueryService;
import umc.demoday.whatisthis.global.CustomUserDetails;
import umc.demoday.whatisthis.global.apiPayload.CustomResponse;

import static umc.demoday.whatisthis.domain.notice.code.NoticeSuccessCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/notices")
public class AdminNoticeController {

    private final NoticeCommandService noticeCommandService;
    private final NoticeQueryService noticeQueryService;

    @PostMapping
    @Operation(summary = "공지사항 작성 api -by 윤영석")
    public CustomResponse<Void> createNotice(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestBody NoticeCreateReqDTO dto) {
        noticeCommandService.createNotice(dto, principal.getId());
        return CustomResponse.onSuccess(NOTICE_CREATE_OK, null);
    }

    @GetMapping
    @Operation(summary = "공지사항 목록 기본: 5개씩 -by 윤영석")
    public CustomResponse<NoticePageResDTO> getNoticeList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size, Sort.by(Sort.Direction.DESC, "createdAt"));

        NoticePageResDTO result = noticeQueryService.getNoticeList(pageable);
        return CustomResponse.onSuccess(NOTICE_OK, result);
    }

    @GetMapping("/{noticeId}")
    @Operation(summary = "공지사항 상세조회 -by 윤영석")
    public CustomResponse<NoticeResDTO> getNotice(@PathVariable("noticeId") Integer noticeId) {
        NoticeResDTO result = noticeQueryService.getNotice(noticeId);
        return CustomResponse.onSuccess(NOTICE_OK, result);
    }

    @PatchMapping("/{noticeId}")
    @Operation(summary = "공지사항 수정 api -by 윤영석")
    public CustomResponse<Void> updateNotice(
            @PathVariable("noticeId") Integer noticeId,
            @RequestBody NoticeUpdateReqDTO dto) {

        noticeCommandService.updateNotice(noticeId, dto);
        return CustomResponse.onSuccess(NOTICE_UPDATE_OK, null);
    }

    @DeleteMapping("/{noticeId}")
    @Operation(summary = "공지사항 삭제 api -by 윤영석")
    public CustomResponse<Void> deleteNotice(@PathVariable("noticeId") Integer noticeId) {
        noticeCommandService.deleteNotice(noticeId);
        return CustomResponse.onSuccess(NOTICE_DELETE_OK, null);
    }
}
