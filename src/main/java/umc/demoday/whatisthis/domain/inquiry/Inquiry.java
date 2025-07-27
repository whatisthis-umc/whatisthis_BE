package umc.demoday.whatisthis.domain.inquiry;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import umc.demoday.whatisthis.domain.answer.Answer;
import umc.demoday.whatisthis.domain.inquiry.enums.InquiryStatus;
import umc.demoday.whatisthis.domain.member.Member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "inquiry")
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "is_secret", nullable = false)
    private Boolean isSecret;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private InquiryStatus status;

    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "inquiry", cascade = CascadeType.ALL)
    private List<Answer> answers = new ArrayList<>();

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}