package umc.demoday.whatisthis.domain.inquiry.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umc.demoday.whatisthis.domain.inquiry.dto.reqDTO.InquiryCreateReqDTO;
import umc.demoday.whatisthis.domain.inquiry.dto.resDTO.InquiryAdminPageResDTO;
import umc.demoday.whatisthis.domain.inquiry.dto.resDTO.InquiryPageResDTO;
import umc.demoday.whatisthis.domain.inquiry.dto.resDTO.InquiryResDTO;
import umc.demoday.whatisthis.domain.inquiry.service.InquiryCommandService;
import umc.demoday.whatisthis.domain.inquiry.service.InquiryQueryService;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.global.apiPayload.CustomResponse;
import umc.demoday.whatisthis.global.service.S3Service;

import java.util.Collections;
import java.util.List;

import static umc.demoday.whatisthis.domain.inquiry.code.InquirySuccessCode.INQUIRY_OK;


@Slf4j
@RestController
@RequestMapping("/support/inquiries")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryCommandService inquiryCommandService;
    private final InquiryQueryService inquiryQueryService;
    private final S3Service s3Service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "문의내역 작성 api-by 윤영석")
    public CustomResponse<Void> createInquiry(
            @RequestPart("request") @Valid InquiryCreateReqDTO dto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @AuthenticationPrincipal Member loginUser) {

        log.info("member : {}", loginUser);

        // S3에 파일 업로드
        List<String> fileUrls = (files != null && !files.isEmpty())
                ? s3Service.uploadFiles(files, "inquiry")
                : Collections.emptyList();

        // 서비스 로직 호출 (파일 URL 함께 전달)
        inquiryCommandService.createInquiry(dto, fileUrls, loginUser);

        return CustomResponse.ok(null);
    }

    @GetMapping
    @Operation(summary = "문의내역 목록조회 api -by 윤영석")
    public CustomResponse<InquiryPageResDTO> getInquiryList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size, Sort.by(Sort.Direction.DESC, "createdAt"));

        InquiryPageResDTO result = inquiryQueryService.getInquiryList(pageable);

        return CustomResponse.onSuccess(INQUIRY_OK, result);
    }

    @GetMapping("/{inquiryId}")
    @Operation(summary = "문의내역 상세조회 API -by 윤영석")
    public CustomResponse<InquiryResDTO> getInquiry(
            @PathVariable int inquiryId,
            @AuthenticationPrincipal Member loginUser) {

        InquiryResDTO result = inquiryQueryService.getInquiry(inquiryId, loginUser);
        return CustomResponse.onSuccess(INQUIRY_OK, result);
    }
}
