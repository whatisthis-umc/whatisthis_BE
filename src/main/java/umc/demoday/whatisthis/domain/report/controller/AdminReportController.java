package umc.demoday.whatisthis.domain.report.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.demoday.whatisthis.domain.report.dto.AdminReportRequestDTO;
import umc.demoday.whatisthis.domain.report.dto.AdminReportResponseDTO;
import umc.demoday.whatisthis.domain.report.enums.ReportStatus;
import umc.demoday.whatisthis.domain.report.enums.RequestReportStatus;
import umc.demoday.whatisthis.global.apiPayload.CustomResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/reports")
public class AdminReportController {

    @GetMapping
    @Operation(summary = "신고 내역 리스트 조회 API -by 남성현")
    public CustomResponse<AdminReportResponseDTO.reportListResponseDTO> getReportList(
            @Parameter(description = "") @RequestParam RequestReportStatus status,
            @Parameter(description = "검색 키워드 null 가능") @RequestParam(required = false) String keyword) {
        return null;
    }

    @GetMapping("/{report-id}")
    @Operation(summary = "신고 내역 상세조회 API -by 남성현")
    public CustomResponse<AdminReportResponseDTO.reportDetailResponseDTO> getReportDetail(
            @Parameter(description = "신고 id") @PathVariable(name = "report-id") Integer reportId) {
        return null;
    }

    @PostMapping("/{report-id}")
    @Operation(summary = "신고 내역 처리 API -by 남성현")
    public CustomResponse<AdminReportResponseDTO.ProcessResponseDTO> getReportDetail(
            @Parameter(description = "신고 id") @PathVariable(name = "report-id") Integer reportId,
            @Valid @RequestBody AdminReportRequestDTO.ProcessRequestDTO request) {
        return null;
    }


}
