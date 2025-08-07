package umc.demoday.whatisthis.domain.recommendation;

import aj.org.objectweb.asm.commons.Remapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SyncStatusRepository extends JpaRepository<SyncStatus, String> {
    Optional<SyncStatus> findBySyncId(String syncId);
}
