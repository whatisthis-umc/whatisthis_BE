package umc.demoday.whatisthis.domain.report.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import umc.demoday.whatisthis.domain.report.Report;
import umc.demoday.whatisthis.domain.report.dto.AdminReportRequestDTO;
import umc.demoday.whatisthis.domain.report.dto.AdminReportResponseDTO;
import umc.demoday.whatisthis.domain.report.enums.ReportStatus;
import umc.demoday.whatisthis.domain.report.enums.RequestReportStatus;
import umc.demoday.whatisthis.domain.report.service.AdminReportService;
import umc.demoday.whatisthis.global.apiPayload.CustomResponse;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralSuccessCode;

import static umc.demoday.whatisthis.domain.report.converter.ReportConverter.toReportListResponseDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/reports")
public class AdminReportController {

    private final AdminReportService adminReportService;

    @GetMapping
    @Operation(summary = "신고 내역 리스트 조회 API -by 남성현")
    public CustomResponse<AdminReportResponseDTO.ReportListResponseDTO> getReportList(
            @Parameter(description = "페이지 번호") @RequestParam Integer page,
            @Parameter(description = "상태 (전체, 미처리, 처리완료)") @RequestParam RequestReportStatus status,
            @Parameter(description = "검색 키워드 null 가능") @RequestParam(required = false) String keyword) {

        Page<Report> reports = adminReportService.reportList(page-1, status, keyword);

        return CustomResponse.onSuccess(GeneralSuccessCode.OK,toReportListResponseDTO(reports));
    }

    @GetMapping("/{report-id}")
    @Operation(summary = "신고 내역 상세조회 API -by 남성현")
    public CustomResponse<AdminReportResponseDTO.ReportDetailResponseDTO> getReportDetail(
            @Parameter(description = "신고 id") @PathVariable(name = "report-id") Integer reportId) {

        Report report = adminReportService.getReport(reportId);

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
