package umc.demoday.whatisthis.domain.notice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.demoday.whatisthis.domain.notice.dto.reqDTO.NoticeCreateReqDTO;
import umc.demoday.whatisthis.domain.notice.service.NoticeCommandService;
import umc.demoday.whatisthis.global.apiPayload.CustomResponse;

import static umc.demoday.whatisthis.domain.notice.code.NoticeSuccessCode.NOTICE_CREATE_OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminNoticeController {

    private final NoticeCommandService noticeCommandService;

    @PostMapping("/notices")
    public CustomResponse<Void> createNotice(@RequestBody NoticeCreateReqDTO dto) {
        noticeCommandService.createNotice(dto);
        return CustomResponse.onSuccess(NOTICE_CREATE_OK, null);
    }
}
