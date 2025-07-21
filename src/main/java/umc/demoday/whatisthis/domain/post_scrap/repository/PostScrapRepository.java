package umc.demoday.whatisthis.domain.post_scrap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post_scrap.PostScrap;

public interface PostScrapRepository extends JpaRepository<PostScrap, Integer> {
    int countByPostId(Integer postId);
}
