package umc.demoday.whatisthis.domain.qna.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc.demoday.whatisthis.domain.qna.Qna;

@Repository
public interface QnaRepository extends JpaRepository<Qna, Integer> {

}
