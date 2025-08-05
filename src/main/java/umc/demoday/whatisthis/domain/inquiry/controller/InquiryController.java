package umc.demoday.whatisthis.domain.inquiry.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umc.demoday.whatisthis.domain.inquiry.dto.reqDTO.InquiryCreateReqDTO;
import umc.demoday.whatisthis.domain.inquiry.service.InquiryCommandService;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.global.apiPayload.CustomResponse;
import umc.demoday.whatisthis.global.service.S3Service;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/support/inquiries")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryCommandService inquiryCommandService;
    private final S3Service s3Service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CustomResponse<Void> createInquiry(
            @RequestPart("request") @Valid InquiryCreateReqDTO dto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @AuthenticationPrincipal Member loginUser) {

        // S3에 파일 업로드
        List<String> fileUrls = (files != null && !files.isEmpty())
                ? s3Service.uploadFiles(files, "inquiry")
                : Collections.emptyList();

        // 서비스 로직 호출 (파일 URL 함께 전달)
        inquiryCommandService.createInquiry(dto, fileUrls, loginUser);

        return CustomResponse.ok(null);
    }
}
