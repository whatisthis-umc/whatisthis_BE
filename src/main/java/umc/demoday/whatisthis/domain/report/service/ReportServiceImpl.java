package umc.demoday.whatisthis.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.demoday.whatisthis.domain.report.Report;
import umc.demoday.whatisthis.domain.report.repository.ReportRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;

    @Override
    public Report insertNewReport(Report report) {
        return reportRepository.save(report);
    }
}
