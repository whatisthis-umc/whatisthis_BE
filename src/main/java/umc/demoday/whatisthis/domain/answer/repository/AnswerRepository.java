package umc.demoday.whatisthis.domain.answer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.demoday.whatisthis.domain.answer.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
}
