package umc.demoday.whatisthis.domain.notice.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import umc.demoday.whatisthis.domain.notice.dto.resDTO.NoticePageResDTO;
import umc.demoday.whatisthis.domain.notice.dto.resDTO.NoticeResDTO;
import umc.demoday.whatisthis.domain.notice.service.NoticeQueryService;
import umc.demoday.whatisthis.global.apiPayload.CustomResponse;

import static umc.demoday.whatisthis.domain.notice.code.NoticeSuccessCode.NOTICE_OK;


@RestController
@RequiredArgsConstructor
@RequestMapping("/support/notices")
public class NoticeController {

    private final NoticeQueryService noticeQueryService;

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
}