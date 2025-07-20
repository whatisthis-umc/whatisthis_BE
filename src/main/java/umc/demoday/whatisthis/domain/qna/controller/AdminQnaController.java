package umc.demoday.whatisthis.domain.qna.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import umc.demoday.whatisthis.domain.qna.dto.reqDTO.QnaCreateReqDTO;
import umc.demoday.whatisthis.domain.qna.dto.reqDTO.QnaUpdateReqDTO;
import umc.demoday.whatisthis.domain.qna.dto.resDTO.QnaPageResDTO;
import umc.demoday.whatisthis.domain.qna.dto.resDTO.QnaResDTO;
import umc.demoday.whatisthis.domain.qna.service.QnaCommandService;
import umc.demoday.whatisthis.domain.qna.service.QnaQueryService;
import umc.demoday.whatisthis.global.apiPayload.CustomResponse;

import static umc.demoday.whatisthis.domain.qna.code.QnaSuccessCode.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/qnas")
public class AdminQnaController {

    private final QnaCommandService qnaCommandService;
    private final QnaQueryService qnaQueryService;

    @PostMapping
    @Operation(summary = "qna 작성 api -by 윤영석")
    public CustomResponse<Void> createQna(@RequestBody QnaCreateReqDTO dto) {
        qnaCommandService.createQna(dto);
        return CustomResponse.onSuccess(QNA_CREATE_OK, null);
    }

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

    @PatchMapping("/{qnaId}")
    @Operation(summary = "qna 수정 api -by 윤영석")
    public CustomResponse<Void> updateQna(
            @PathVariable("qnaId") Integer qnaId,
            @RequestBody QnaUpdateReqDTO dto) {

        qnaCommandService.updateQna(qnaId, dto);
        return CustomResponse.onSuccess(QNA_UPDATE_OK, null);
    }

    @DeleteMapping("/{qnaId}")
    @Operation(summary = "qna 삭제 api -by 윤영석")
    public CustomResponse<Void> deleteQna(@PathVariable("qnaId") Integer qnaId) {
        qnaCommandService.deleteQna(qnaId);
        return CustomResponse.onSuccess(QNA_DELETE_OK, null);
    }
}
