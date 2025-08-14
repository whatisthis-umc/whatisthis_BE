package umc.demoday.whatisthis.domain.refresh_token;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "admin_refresh_token")
public class AdminRefreshToken {

    @Id
    private Integer adminId;

    private String token;

    public void updateToken(String token) {
        this.token = token;
    }
}
