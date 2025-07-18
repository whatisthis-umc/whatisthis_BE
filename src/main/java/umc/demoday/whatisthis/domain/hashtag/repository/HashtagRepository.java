package umc.demoday.whatisthis.domain.hashtag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.demoday.whatisthis.domain.hashtag.Hashtag;
import umc.demoday.whatisthis.domain.post.Post;

import java.util.Collection;

public interface HashtagRepository extends JpaRepository<Hashtag, Integer> {
    Collection<Object> findAllByPost(Post post);
}
