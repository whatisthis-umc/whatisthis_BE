package umc.demoday.whatisthis.domain.admin.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.demoday.whatisthis.domain.admin.dto.AdminHomeInfoResDTO;
import umc.demoday.whatisthis.domain.comment.repository.CommentRepository;
import umc.demoday.whatisthis.domain.inquiry.enums.InquiryStatus;
import umc.demoday.whatisthis.domain.inquiry.repository.InquiryRepository;
import umc.demoday.whatisthis.domain.member.repository.MemberRepository;
import umc.demoday.whatisthis.domain.post.repository.PostRepository;
import umc.demoday.whatisthis.domain.report.enums.ReportStatus;
import umc.demoday.whatisthis.domain.report.repository.ReportRepository;

@Service
@RequiredArgsConstructor
public class AdminHomeServiceImpl implements AdminHomeService {

    private final ReportRepository reportRepository;
    private final InquiryRepository inquiryRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Override
    public AdminHomeInfoResDTO getAdminHomeInfo(){
        return AdminHomeInfoResDTO.builder()
                .reports(AdminHomeInfoResDTO.Reports.builder()
                        .unprocessedCount(reportRepository.countByStatus(ReportStatus.UNPROCESSED))
                        .build())
                .inquiries(AdminHomeInfoResDTO.Inquiries.builder()
                        .unansweredCount(inquiryRepository.countByStatus(InquiryStatus.UNPROCESSED))
                        .build())
                .userStats(AdminHomeInfoResDTO.UserStats.builder()
                        .totalUserCount(memberRepository.count())
                        .todaySignupCount(memberRepository.countTodaySignups())
                        .build())
                .contentStats(AdminHomeInfoResDTO.ContentStats.builder()
                        .postCount(postRepository.count())
                        .commentCount(commentRepository.count())
                        .build())
                .build();
    }
}
