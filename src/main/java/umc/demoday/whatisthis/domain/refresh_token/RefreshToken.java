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
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    private Integer memberId;

    private String token;

    public void updateToken(String token) {
        this.token = token;
    }
}
