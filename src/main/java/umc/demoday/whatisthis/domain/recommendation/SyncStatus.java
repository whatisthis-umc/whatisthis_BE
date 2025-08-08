package umc.demoday.whatisthis.domain.recommendation;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SyncStatus {

    @Id()
    @Column(nullable = false)
    private String syncId; // "postSync" 와 같은 고유 키

    @Column(nullable = false)
    private LocalDateTime lastProcessedAt;

}
