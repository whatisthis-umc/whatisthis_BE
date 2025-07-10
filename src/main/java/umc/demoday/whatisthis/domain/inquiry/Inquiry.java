package umc.demoday.whatisthis.domain.inquiry;
import jakarta.persistence.*;
import lombok.*;
import umc.demoday.whatisthis.domain.answer.Answer;
import umc.demoday.whatisthis.domain.member.Member;

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

    @Column(nullable = false, length = 20)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "inquiry", cascade = CascadeType.ALL)
    private List<Answer> answers = new ArrayList<>();
}