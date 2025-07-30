package umc.demoday.whatisthis.domain.hashtag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.demoday.whatisthis.domain.hashtag.Hashtag;
import umc.demoday.whatisthis.domain.post.Post;

import java.util.Collection;
import java.util.List;

public interface HashtagRepository extends JpaRepository<Hashtag, Integer> {
    List<Hashtag> findAllByPostId(Integer postId);
    List<Hashtag> findAllByPost_IdIn(List<Integer> postIds);

    void deleteAllByPost(Post post);
}
