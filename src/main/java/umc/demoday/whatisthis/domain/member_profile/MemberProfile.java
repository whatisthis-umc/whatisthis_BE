package umc.demoday.whatisthis.domain.member_profile;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import lombok.*;
import umc.demoday.whatisthis.domain.admin.Admin;
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

    // Member 엔티티
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, unique = true)
    private Member member;

    // 마지막으로 본 게시물의 ID
    private Integer lastSeenPostId;


    public void updateLastSeenPost(Integer postId) {
        this.lastSeenPostId = postId;
    }
}