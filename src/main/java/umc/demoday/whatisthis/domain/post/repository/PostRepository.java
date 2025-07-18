package umc.demoday.whatisthis.domain.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import umc.demoday.whatisthis.domain.post.Post;

import java.time.LocalDateTime;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findAllByCategory(String category, Pageable pageable);

    Page<Post> findByCreatedAtAfter(LocalDateTime createdAtAfter, Pageable pageable);
}
