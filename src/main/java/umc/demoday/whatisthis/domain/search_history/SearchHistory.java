package umc.demoday.whatisthis.domain.search_history;
import jakarta.persistence.*;
import lombok.*;
import umc.demoday.whatisthis.domain.member.Member;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "search_history")
public class SearchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20, nullable = false)
    private String keyword;

    @Column(name = "searched_at", nullable = false)
    private LocalDateTime searchedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
}