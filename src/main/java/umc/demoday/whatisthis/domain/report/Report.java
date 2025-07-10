package umc.demoday.whatisthis.domain.report;
import jakarta.persistence.*;
import lombok.*;
import umc.demoday.whatisthis.domain.comment.Comment;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.post.Post;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20, nullable = false)
    private String status;

    @Column(length = 20, nullable = false)
    private String content;

    @Column(name = "reported_at")
    private LocalDateTime reportedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;
}