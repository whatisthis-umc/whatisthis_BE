package umc.demoday.whatisthis.domain.refresh_token.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.demoday.whatisthis.domain.refresh_token.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
}
