package umc.demoday.whatisthis.domain.profile_image;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import umc.demoday.whatisthis.domain.member.Member;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "profile_image")
public class ProfileImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    @OneToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;
}
