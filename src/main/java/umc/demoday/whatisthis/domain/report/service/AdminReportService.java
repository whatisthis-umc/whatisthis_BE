package umc.demoday.whatisthis.domain.report.service;

import org.springframework.data.domain.Page;
import umc.demoday.whatisthis.domain.report.Report;
import umc.demoday.whatisthis.domain.report.enums.RequestReportStatus;

public interface AdminReportService {

    Page<Report> reportList(Integer page, RequestReportStatus status, String keyword);
    Report getReport(Integer reportId);
    Report processReport(Report report, Boolean deleteRequest);
}
