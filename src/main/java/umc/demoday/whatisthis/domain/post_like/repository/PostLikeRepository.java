package umc.demoday.whatisthis.domain.post_like.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post_like.PostLike;

import java.util.List;

public interface PostLikeRepository extends JpaRepository<PostLike, Integer> {
    Integer countByMember(Member member);

    boolean existsByMemberAndPost(Member member, Post post);

    void deletePostLikeByPostAndMember(Post post, Member member);

    // 특정 회원이 좋아요한 PostLike 리스트 조회
    Page<PostLike> findByMember(Member member, Pageable pageable);
}
