package umc.demoday.whatisthis.domain.admin;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "admin_id", nullable = false, length = 20)
    private String adminId; // 관리자가 입력한 아이디

    @Column(nullable = false, length = 100)
    private String password;
}

