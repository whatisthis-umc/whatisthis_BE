package umc.demoday.whatisthis.domain.post_like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post_like.PostLike;

public interface PostLikeRepository extends JpaRepository<PostLike, Integer> {
    Integer countByMember(Member member);

    boolean existsByMemberAndPost(Member member, Post post);

    void deletePostLikeByPostAndMember(Post post, Member member);
}
