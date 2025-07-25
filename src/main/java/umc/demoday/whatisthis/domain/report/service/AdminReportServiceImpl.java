package umc.demoday.whatisthis.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.demoday.whatisthis.domain.report.Report;
import umc.demoday.whatisthis.domain.report.enums.ReportStatus;
import umc.demoday.whatisthis.domain.report.enums.RequestReportStatus;
import umc.demoday.whatisthis.domain.report.repository.ReportRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminReportServiceImpl implements AdminReportService {

    private final ReportRepository reportRepository;

    @Override
    public Page<Report> reportList(Integer page, RequestReportStatus status, String keyword) {

        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "reportedAt"));

        boolean hasKeyword = keyword != null && !keyword.isBlank();

        if (status == RequestReportStatus.ALL) {
            return hasKeyword
                    ? reportRepository.findByKeyword(keyword, pageable)
                    : reportRepository.findAll(pageable);
        }

        ReportStatus reportStatus = (status == RequestReportStatus.PROCESSED)
                ? ReportStatus.PROCESSED
                : ReportStatus.UNPROCESSED;

        return hasKeyword
                ? reportRepository.findByStatusAndKeyword(reportStatus, keyword, pageable)
                : reportRepository.findByStatus(reportStatus, pageable);
    }

    @Override
    public Report getReport(Integer reportId) {
        return null;
    }

    @Override
    public Report processReport(Report report, Boolean deleteRequest) {
        return null;
    }
}
