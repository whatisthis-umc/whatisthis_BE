package umc.demoday.whatisthis.domain.post_image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post_image.PostImage;

import java.util.Collection;
import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Integer> {
    List<PostImage> findAllByPostId(Integer postId);
    PostImage findOneByPostId(Integer postId);

    List<PostImage> findAllByPost_IdIn(List<Integer> postIds);

    void deleteAllByPost(Post post);
}
