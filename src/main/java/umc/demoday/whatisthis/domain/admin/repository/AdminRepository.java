package umc.demoday.whatisthis.domain.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.demoday.whatisthis.domain.admin.Admin;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByAdminId(String adminId);
}
