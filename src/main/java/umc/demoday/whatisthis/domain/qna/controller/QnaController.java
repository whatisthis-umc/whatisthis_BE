package umc.demoday.whatisthis.domain.qna.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import umc.demoday.whatisthis.domain.qna.dto.resDTO.QnaPageResDTO;
import umc.demoday.whatisthis.domain.qna.dto.resDTO.QnaResDTO;
import umc.demoday.whatisthis.domain.qna.service.QnaQueryService;
import umc.demoday.whatisthis.global.apiPayload.CustomResponse;


import static umc.demoday.whatisthis.domain.qna.code.QnaSuccessCode.QNA_OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/support/qnas")
public class QnaController {

    private final QnaQueryService qnaQueryService;

    @GetMapping
    @Operation(summary = "qna 목록 기본: 5개씩 -by 윤영석")
    public CustomResponse<QnaPageResDTO> getQnaList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size, Sort.by(Sort.Direction.DESC, "createdAt"));

        QnaPageResDTO result = qnaQueryService.getQnaList(pageable);
        return CustomResponse.onSuccess(QNA_OK, result);
    }

    @GetMapping("/{qnaId}")
    @Operation(summary = "qna 상세조회 -by 윤영석")
    public CustomResponse<QnaResDTO> getQna(@PathVariable("qnaId") Integer qnaId) {
        QnaResDTO result = qnaQueryService.getQna(qnaId);
        return CustomResponse.onSuccess(QNA_OK, result);
    }
}
