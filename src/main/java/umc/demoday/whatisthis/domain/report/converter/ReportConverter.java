package umc.demoday.whatisthis.domain.report.converter;

import org.springframework.data.domain.Page;
import umc.demoday.whatisthis.domain.comment.Comment;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.report.Report;
import umc.demoday.whatisthis.domain.report.dto.AdminReportResponseDTO;
import umc.demoday.whatisthis.domain.report.dto.ReportRequestDTO;
import umc.demoday.whatisthis.domain.report.dto.ReportResponseDTO;
import umc.demoday.whatisthis.domain.report.enums.ReportStatus;

import java.util.List;
import java.util.stream.Collectors;

public class ReportConverter {

    public static Report toReport(ReportRequestDTO.NewReportRequestDTO request, Member member, Post post, Comment comment) {

        return Report.builder()
                .status(ReportStatus.UNPROCESSED)
                .content(request.getContent())
                .description(request.getDescription())
                .member(member)
                .post(post)
                .comment(comment)
                .build();
    }

    public static ReportResponseDTO.ReportPostResponseDTO toReportPostResponseDTO(Report report) {

        return ReportResponseDTO.ReportPostResponseDTO.builder()
                .reportId(report.getId())
                .postId(report.getPost().getId())
                .reportedAt(report.getReportedAt())
                .build();
    }

    public static ReportResponseDTO.ReportCommentResponseDTO toReportCommentResponseDTO(Report report) {

        return ReportResponseDTO.ReportCommentResponseDTO.builder()
                .reportId(report.getId())
                .commentId(report.getComment().getId())
                .reportedAt(report.getReportedAt())
                .build();
    }

    public static AdminReportResponseDTO.ReportPageResponseDTO toReportPageResponseDTO(Report report) {

        String type = "";
        String content = "";

        if (report.getPost() == null && report.getComment() != null) {
            type = "COMMENT";
            content = report.getComment().getContent();
        }
        else if (report.getPost() != null && report.getComment() == null) {
            type = "POST";
            content = report.getPost().getContent();
        }

        return AdminReportResponseDTO.ReportPageResponseDTO.builder()
                .reportId(report.getId())
                .type(type)
                .content(content)
                .reportContent(report.getContent())
                .reportedAt(report.getReportedAt())
                .status(report.getStatus())
                .build();
    }

    public static AdminReportResponseDTO.ReportListResponseDTO toReportListResponseDTO(Page<Report> reportList) {

        List<AdminReportResponseDTO.ReportPageResponseDTO> ReportDTOList = reportList.stream()
                .map(ReportConverter::toReportPageResponseDTO).collect(Collectors.toList());

        return AdminReportResponseDTO.ReportListResponseDTO.builder()
                .reportList(ReportDTOList)
                .listSize(reportList.getSize())
                .isFirst(reportList.isFirst())
                .isLast(reportList.isLast())
                .totalPage(reportList.getTotalPages())
                .totalElements(reportList.getTotalElements())
                .build();
    }

}
