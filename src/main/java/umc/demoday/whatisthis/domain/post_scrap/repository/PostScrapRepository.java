package umc.demoday.whatisthis.domain.post_scrap.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post_scrap.PostScrap;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface PostScrapRepository extends JpaRepository<PostScrap, Integer> {
    int countByPostId(Integer postId);

    @Query("SELECT ps.post.id, COUNT(ps) " + "FROM PostScrap ps " + "WHERE ps.post.id IN :postIds " + "GROUP BY ps.post.id")
    List<Object[]> findScrapCountsByPostIds(@Param("postIds") List<Integer> postIds);
}
