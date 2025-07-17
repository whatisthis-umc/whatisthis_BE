package umc.demoday.whatisthis.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.demoday.whatisthis.domain.post.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {
}
