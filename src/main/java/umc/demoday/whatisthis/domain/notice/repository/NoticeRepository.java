package umc.demoday.whatisthis.domain.notice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.demoday.whatisthis.domain.notice.Notice;
import umc.demoday.whatisthis.domain.notice.dto.NoticeResDTO;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {

}
