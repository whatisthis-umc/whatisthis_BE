package umc.demoday.whatisthis.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.demoday.whatisthis.domain.comment.Comment;
import umc.demoday.whatisthis.domain.comment.repository.CommentRepository;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.repository.PostRepository;
import umc.demoday.whatisthis.domain.report.Report;
import umc.demoday.whatisthis.domain.report.code.ReportErrorCode;
import umc.demoday.whatisthis.domain.report.enums.ReportStatus;
import umc.demoday.whatisthis.domain.report.enums.RequestReportStatus;
import umc.demoday.whatisthis.domain.report.repository.ReportRepository;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;

import static umc.demoday.whatisthis.domain.report.code.ReportErrorCode.ALREADY_PROCESSED_REPORT;
import static umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode.BAD_REQUEST_400;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminReportServiceImpl implements AdminReportService {

    private final ReportRepository reportRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

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
        return reportRepository.findById(reportId).orElseThrow(()-> new GeneralException(ReportErrorCode.REPORT_NOT_FOUND));
    }

    @Override
    public Report processReport(Report report, Boolean deleteRequest) {

        if (report.getStatus() == ReportStatus.PROCESSED){
            throw new GeneralException(ALREADY_PROCESSED_REPORT);
        }

        if (deleteRequest) {

            if (report.getPost() == null && report.getComment() != null){
                Comment c = report.getComment();

                if(c.getIsDeleted()){
                    throw new GeneralException(ALREADY_PROCESSED_REPORT);
                }

                c.setIsDeleted(true);
                commentRepository.save(c);
            }

            else if (report.getPost() != null && report.getComment() == null){

                Post post = report.getPost();

                reportRepository.detachAllByPostId(post.getId());
                reportRepository.flush();
                postRepository.delete(post);

                report.setPost(null);
                reportRepository.save(report);
            }

            else if (report.getPost() == null && report.getComment() == null){

                if (report.getStatus() == ReportStatus.PROCESSED){
                    throw new GeneralException(ALREADY_PROCESSED_REPORT);
                }
                else {throw new GeneralException(BAD_REQUEST_400);}

            }

        }

        report.setStatus(ReportStatus.PROCESSED);

        return reportRepository.save(report);
    }
}
