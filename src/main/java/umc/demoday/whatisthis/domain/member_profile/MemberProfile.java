package umc.demoday.whatisthis.domain.member_profile;


import jakarta.persistence.*;
import lombok.*;
import umc.demoday.whatisthis.domain.member.Member;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Member 엔티티와 1:1 관계. 이 프로필의 주인을 명시
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, unique = true)
    private Member member;

    // 마지막으로 본 게시물의 ID
    private Integer lastSeenPostId;


    public void updateLastSeenPost(Integer postId) {
        this.lastSeenPostId = postId;
    }
}