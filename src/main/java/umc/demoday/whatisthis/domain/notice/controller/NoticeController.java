package umc.demoday.whatisthis.domain.notice.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import umc.demoday.whatisthis.domain.notice.Notice;
import umc.demoday.whatisthis.domain.notice.dto.NoticePageResDTO;
import umc.demoday.whatisthis.domain.notice.dto.NoticeResDTO;
import umc.demoday.whatisthis.domain.notice.service.NoticeQueryService;
import umc.demoday.whatisthis.global.apiPayload.CustomResponse;

import static umc.demoday.whatisthis.global.apiPayload.code.GeneralSuccessCode.NOTICE_OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/support")
public class NoticeController {

    private final NoticeQueryService noticeQueryService;

    @GetMapping("/notices")
    @Operation(summary = "공지사항 목록 기본: 5개씩 -by 윤영석")
    public CustomResponse<NoticePageResDTO> getNoticeList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size, Sort.by(Sort.Direction.DESC, "createdAt"));

        NoticePageResDTO result = noticeQueryService.getNoticeList(pageable);
        return CustomResponse.onSuccess(NOTICE_OK, result);
    }

    @GetMapping("/notices/{noticeId}")
    public CustomResponse<NoticeResDTO> getNotice(@PathVariable("noticeId") Integer noticeId) {
        NoticeResDTO result = noticeQueryService.getNotice(noticeId);
        return CustomResponse.onSuccess(NOTICE_OK, result);
    }
}