package umc.demoday.whatisthis.domain.recommendation;

import aj.org.objectweb.asm.commons.Remapper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SyncStatusRepository extends JpaRepository<SyncStatus, String> {
    SyncStatus findBySyncId(String syncId);
}
