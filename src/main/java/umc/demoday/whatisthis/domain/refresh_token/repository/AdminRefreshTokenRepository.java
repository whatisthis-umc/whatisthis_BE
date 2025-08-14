package umc.demoday.whatisthis.domain.refresh_token.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.demoday.whatisthis.domain.refresh_token.AdminRefreshToken;

public interface AdminRefreshTokenRepository extends JpaRepository<AdminRefreshToken, Integer> {
}
