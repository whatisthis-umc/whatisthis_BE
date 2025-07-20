package umc.demoday.whatisthis.domain.notice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.demoday.whatisthis.domain.notice.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {

}
