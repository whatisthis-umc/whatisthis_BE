package umc.demoday.whatisthis.domain.post_image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post_image.PostImage;

import java.util.Collection;

public interface PostImageRepository extends JpaRepository<PostImage, Integer> {
    Collection<Object> findAllByPost(Post post);
}
