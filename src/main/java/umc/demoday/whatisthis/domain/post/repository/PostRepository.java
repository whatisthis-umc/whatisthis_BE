package umc.demoday.whatisthis.domain.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.enums.Category;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findByCategory(Category category, Pageable pageable); // 이렇게 하면 Pageeble 이용해서 정렬 기준대로 정렬해서 원하는 사이즈만큼 가져온다.
}
