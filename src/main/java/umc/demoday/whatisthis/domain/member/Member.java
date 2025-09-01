package umc.demoday.whatisthis.domain.member;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import umc.demoday.whatisthis.domain.profile_image.ProfileImage;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "member_id", length = 20)
    private String memberId; // 사용자가 입력한 아이디

    @Column(nullable = false, length = 50)
    private String email;

    @Column(length = 100)
    private String password;

    @Column(length = 20, nullable = false)
    private String nickname;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "deleted_at", nullable = false)
    private LocalDateTime deletedAt;

    @Column(name = "service_agreed", nullable = false)
    private Boolean serviceAgreed;

    @Column(name = "privacy_agreed", nullable = false)
    private Boolean privacyAgreed;

    @Column(name = "is_best", nullable = false)
    @ColumnDefault("false")
    private Boolean isBest;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "provider")
    private String provider; // kakao, google, naver

    @Column(name = "provider_id")
    private String providerId;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profile_image_id", referencedColumnName = "id")
    private ProfileImage profileImage;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.isDeleted == null) {
            this.isDeleted = false;
        }
        if (this.deletedAt == null) {
            this.deletedAt = LocalDateTime.of(9999, 12, 31, 23, 59, 59);
        }
        if (this.isBest == null) {
            this.isBest = false;
        }
    }

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void linkSocial(String provider, String providerId) {
        this.provider = provider;
        this.providerId = providerId;
    }
}