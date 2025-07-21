package umc.demoday.whatisthis.domain.report.converter;

import umc.demoday.whatisthis.domain.comment.Comment;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.report.Report;
import umc.demoday.whatisthis.domain.report.dto.ReportRequestDTO;
import umc.demoday.whatisthis.domain.report.dto.ReportResponseDTO;
import umc.demoday.whatisthis.domain.report.enums.ReportStatus;

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
}
