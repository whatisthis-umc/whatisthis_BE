package umc.demoday.whatisthis.domain.recommendation;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class SyncStatus {

    @Id
    private String syncId; // "postSync" 와 같은 고유 키

    private LocalDateTime lastProcessedAt;

}
