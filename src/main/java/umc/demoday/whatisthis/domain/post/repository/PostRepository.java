package umc.demoday.whatisthis.domain.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.enums.Category;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findAllByCategory(Category category, Pageable pageable);
    Page<Post> findByCreatedAtAfter(LocalDateTime createdAtAfter, Pageable pageable);

    Integer countByMemberIdAndCreatedAtAfter(Integer memberId, LocalDateTime createdAtAfter);
    Page<Post> findByCategory(Category category, Pageable pageable); // 이렇게 하면 Pageeble 이용해서 정렬 기준대로 정렬해서 원하는 사이즈만큼 가져온다.

    @Modifying
    @Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :postId")
    void increaseViewCount(@Param("postId") Integer postId);

    @Modifying
    @Query("UPDATE Post p SET p.likeCount = p.likeCount + 1 WHERE p.id = :postId")
    void increaseLikeCount(@Param("postId") Integer postId);

    @Modifying
    @Query("UPDATE Post p SET p.likeCount = p.likeCount - 1 WHERE p.id = :postId")
    void decreaseLikeCount(@Param("postId") Integer postId);

    Page<Post> findByCategoryIn(Collection<Category> categories, Pageable pageable);

    Page<Post> findByCreatedAtAfterAndCategoryIn(LocalDateTime createdAtAfter, Collection<Category> categories, Pageable pageable);

    // 여러 카테고리로 조회
    Page<Post> findByCategoryIn(List<Category> categories, Pageable pageable);


    @Query("""
    SELECT p FROM Post p
    JOIN FETCH p.member m
    LEFT JOIN FETCH m.profileImage
    WHERE m = :member
""")
    Page<Post> findAllByMember(@Param("member") Member member, Pageable pageable);

    // UpdatedAt 기준으로 오름차순 정렬 조회
    Page<Post> findByUpdatedAtAfterOrderByUpdatedAtAsc(LocalDateTime lastUpdatedAt, Pageable pageable);

}
