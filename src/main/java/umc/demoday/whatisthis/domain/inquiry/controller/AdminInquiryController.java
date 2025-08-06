package umc.demoday.whatisthis.domain.inquiry.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import umc.demoday.whatisthis.domain.answer.dto.reqDTO.AnswerRegisterReqDTO;
import umc.demoday.whatisthis.domain.answer.service.AnswerCommandService;
import umc.demoday.whatisthis.domain.inquiry.dto.resDTO.InquiryAdminPageResDTO;
import umc.demoday.whatisthis.domain.inquiry.dto.resDTO.InquiryAdminResDTO;
import umc.demoday.whatisthis.domain.inquiry.service.InquiryQueryService;
import umc.demoday.whatisthis.global.apiPayload.CustomResponse;

import static umc.demoday.whatisthis.domain.answer.code.AnswerSuccessCode.ANSWER_REGISTER_OK;
import static umc.demoday.whatisthis.domain.inquiry.code.InquirySuccessCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/inquiries")
public class AdminInquiryController {

    private final AnswerCommandService answerCommandService;
    private final InquiryQueryService inquiryQueryService;



    @GetMapping
    @Operation(summary = "관리자 페이지 문의내역 목록조회 api -by 윤영석")
    public CustomResponse<InquiryAdminPageResDTO> getAdminInquiryList(
            @RequestParam(defaultValue = "ALL") String statusStr,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size, Sort.by(Sort.Direction.DESC, "createdAt"));

        InquiryAdminPageResDTO result = inquiryQueryService.getAdminInquiryList(pageable, statusStr);

        return CustomResponse.onSuccess(INQUIRY_OK, result);
    }

    @GetMapping("/{inquiryId}")
    @Operation(summary = "문의내역 상세조회 api-by 윤영석")
    public CustomResponse<InquiryAdminResDTO> getInquiry(@PathVariable("inquiryId") Integer inquiryId) {
        InquiryAdminResDTO result = inquiryQueryService.getAdminInquiry(inquiryId);
        return CustomResponse.onSuccess(INQUIRY_OK, result);
    }

    @PostMapping("/{inquiryId}/answer")
    @Operation(summary = "문의내역에 답변 달기 api -by 윤영석")
    public CustomResponse<Void> registerAnswer(
            @PathVariable Integer inquiryId,
            @RequestBody AnswerRegisterReqDTO answerRegisterReqDTO) {

        answerCommandService.registerAnswer(inquiryId, answerRegisterReqDTO);
        return CustomResponse.onSuccess(ANSWER_REGISTER_OK, null);
    }


}
